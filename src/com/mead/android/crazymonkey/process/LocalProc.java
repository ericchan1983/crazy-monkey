package com.mead.android.crazymonkey.process;

import static org.apache.commons.io.output.NullOutputStream.NULL_OUTPUT_STREAM;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.input.NullInputStream;

import com.mead.android.crazymonkey.StreamTaskListener;
import com.mead.android.crazymonkey.util.Utils;

public class LocalProc {
	
	private static final Logger LOGGER = Logger.getLogger(LocalProc.class.getName());
	
	private static final ExecutorService executor = Executors.newCachedThreadPool(new ExceptionCatchingThreadFactory(new NamingThreadFactory(new DaemonThreadFactory(), "Proc.executor")));

    private final Process proc;
    
	private final Thread copier,copier2;
    private final OutputStream out;
    @SuppressWarnings("unused")
	private final String name;

    private final InputStream stdout,stderr;
    private final OutputStream stdin;

    public LocalProc(String cmd, Map<String,String> env, OutputStream out, File workDir) throws IOException {
        this(cmd,Utils.mapToEnv(env),out,workDir);
    }

    public LocalProc(String[] cmd, Map<String,String> env,InputStream in, OutputStream out) throws IOException {
        this(cmd,Utils.mapToEnv(env),in,out);
    }

    public LocalProc(String cmd,String[] env,OutputStream out, File workDir) throws IOException {
        this( Utils.tokenize(cmd), env, out, workDir );
    }

    public LocalProc(String[] cmd,String[] env,OutputStream out, File workDir) throws IOException {
        this(cmd,env,null,out,workDir);
    }

    public LocalProc(String[] cmd,String[] env,InputStream in,OutputStream out) throws IOException {
        this(cmd,env,in,out,null);
    }

    public LocalProc(String[] cmd,String[] env,InputStream in,OutputStream out, File workDir) throws IOException {
        this(cmd,env,in,out,null,workDir);
    }

    /**
     * @param err
     *      null to redirect stderr to stdout.
     */
    public LocalProc(String[] cmd,String[] env,InputStream in,OutputStream out,OutputStream err,File workDir) throws IOException {
        this( calcName(cmd),
              stderr(environment(new ProcessBuilder(cmd),env).directory(workDir), err==null || err== SELFPUMP_OUTPUT),
              in, out, err );
    }

    private static ProcessBuilder stderr(ProcessBuilder pb, boolean redirectError) {
        if(redirectError)    pb.redirectErrorStream(true);
        return pb;
    }

    private static ProcessBuilder environment(ProcessBuilder pb, String[] env) {
        if(env!=null) {
            Map<String, String> m = pb.environment();
            m.clear();
            for (String e : env) {
                int idx = e.indexOf('=');
                m.put(e.substring(0,idx),e.substring(idx+1,e.length()));
            }
        }
        return pb;
    }

    private LocalProc( String name, ProcessBuilder procBuilder, InputStream in, OutputStream out, OutputStream err ) throws IOException {
        Logger.getLogger(LocalProc.class.getName()).log(Level.FINE, "Running: {0}", name);
        this.name = name;
        this.out = out;
        /*
        this.cookie = EnvVars.createCookie();
        procBuilder.environment().putAll(cookie);
        */
        this.proc = procBuilder.start();

        InputStream procInputStream = proc.getInputStream();
        if (out==SELFPUMP_OUTPUT) {
            stdout = procInputStream;
            copier = null;
        } else {
            copier = new StreamCopyThread(name+": stdout copier", procInputStream, out);
            copier.start();
            stdout = null;
        }

        if (in == null) {
            // nothing to feed to stdin
            stdin = null;
            proc.getOutputStream().close();
        } else
        if (in==SELFPUMP_INPUT) {
            stdin = proc.getOutputStream();
        } else {
            new StdinCopyThread(name+": stdin copier",in,proc.getOutputStream()).start();
            stdin = null;
        }

        InputStream procErrorStream = proc.getErrorStream();
        if(err!=null) {
            if (err==SELFPUMP_OUTPUT) {
                stderr = procErrorStream;
                copier2 = null;
            } else {
                stderr = null;
                copier2 = new StreamCopyThread(name+": stderr copier", procErrorStream, err);
                copier2.start();
            }
        } else {
            // the javadoc is unclear about what getErrorStream() returns when ProcessBuilder.redirectErrorStream(true),
            //
            // according to the source code, Sun JREs still still returns a distinct reader end of a pipe that needs to be closed.
            // but apparently at least on some IBM JDK5, returned input and error streams are the same.
            // so try to close them smartly
            if (procErrorStream!=procInputStream) {
                procErrorStream.close();
            }
            copier2 = null;
            stderr = null;
        }
    }

    public InputStream getStdout() {
        return stdout;
    }

    public InputStream getStderr() {
        return stderr;
    }

    public OutputStream getStdin() {
        return stdin;
    }

    /**
     * Like {@link #join} but can be given a maximum time to wait.
     * @param timeout number of time units
     * @param unit unit of time
     * @param listener place to send messages if there are problems, incl. timeout
     * @return exit code from the process
     * @throws IOException for the same reasons as {@link #join}
     * @throws InterruptedException for the same reasons as {@link #join}
     * @since 1.363
     */
    public final int joinWithTimeout(final long timeout, final TimeUnit unit,
            final StreamTaskListener listener) throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        if (!latch.await(timeout, unit)) {
                            listener.error("Timeout after " + timeout + " " +
                                    unit.toString().toLowerCase(Locale.ENGLISH));
                            kill();
                        }
                    } catch (InterruptedException x) {
                        x.printStackTrace(listener.error("Failed to join a process"));
                    } catch (IOException x) {
                        x.printStackTrace(listener.error("Failed to join a process"));
                    } catch (RuntimeException x) {
                        x.printStackTrace(listener.error("Failed to join a process"));
                    }
                }
            });
            return join();
        } finally {
            latch.countDown();
        }
    }
    
    /**
     * Waits for the completion of the process.
     */
    public int join() throws InterruptedException, IOException {
        // show what we are waiting for in the thread title
        // since this involves some native work, let's have some soak period before enabling this by default 
        Thread t = Thread.currentThread();
        String oldName = t.getName();
        
        try {
            int r = proc.waitFor();
            // see http://wiki.jenkins-ci.org/display/JENKINS/Spawning+processes+from+build
            // problems like that shows up as infinite wait in join(), which confuses great many users.
            // So let's do a timed wait here and try to diagnose the problem
            if (copier!=null)   copier.join(10*1000);
            if(copier2!=null)   copier2.join(10*1000);
            if((copier!=null && copier.isAlive()) || (copier2!=null && copier2.isAlive())) {
                // looks like handles are leaking.
                // closing these handles should terminate the threads.
                String msg = "Process leaked file descriptors. See http://wiki.jenkins-ci.org/display/JENKINS/Spawning+processes+from+build for more information";
                Throwable e = new Exception().fillInStackTrace();
                LOGGER.log(Level.WARNING,msg,e);

                // doing proc.getInputStream().close() hangs in FileInputStream.close0()
                // it could be either because another thread is blocking on read, or
                // it could be a bug in Windows JVM. Who knows.
                // so I'm abandoning the idea of closing the stream
//                try {
//                    proc.getInputStream().close();
//                } catch (IOException x) {
//                    LOGGER.log(Level.FINE,"stdin termination failed",x);
//                }
//                try {
//                    proc.getErrorStream().close();
//                } catch (IOException x) {
//                    LOGGER.log(Level.FINE,"stderr termination failed",x);
//                }
                out.write(msg.getBytes());
                out.write('\n');
            }
            return r;
        } catch (InterruptedException e) {
            // aborting. kill the process
            destroy();
            throw e;
        } finally {
            t.setName(oldName);
        }
    }

    public boolean isAlive() throws IOException, InterruptedException {
        try {
            proc.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    public void kill() throws InterruptedException, IOException {
        destroy();
        join();
    }

    /**
     * Destroys the child process without join.
     */
    private void destroy() throws InterruptedException {
        /* ProcessTree.get().killAll(proc,cookie);*/
    }

    /**
     * {@link Process#getOutputStream()} is buffered, so we need to eagerly flash
     * the stream to push bytes to the process.
     */
    private static class StdinCopyThread extends Thread {
        private final InputStream in;
        private final OutputStream out;

        public StdinCopyThread(String threadName, InputStream in, OutputStream out) {
            super(threadName);
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                try {
                    byte[] buf = new byte[8192];
                    int len;
                    while ((len = in.read(buf)) >= 0) {
                        out.write(buf, 0, len);
                        out.flush();
                    }
                } finally {
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                // TODO: what to do?
            }
        }
    }

    private static String calcName(String[] cmd) {
        StringBuilder buf = new StringBuilder();
        for (String token : cmd) {
            if(buf.length()>0)  buf.append(' ');
            buf.append(token);
        }
        return buf.toString();
    }

    public static final InputStream SELFPUMP_INPUT = new NullInputStream(0);
    public static final OutputStream SELFPUMP_OUTPUT = NULL_OUTPUT_STREAM;

}
