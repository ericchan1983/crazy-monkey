package com.mead.android.crazymonkey.process;

import static org.apache.commons.io.output.NullOutputStream.NULL_OUTPUT_STREAM;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mead.android.crazymonkey.StreamTaskListener;
import com.mead.android.crazymonkey.util.Utils;


public class ProcStarter {

    protected List<String> commands;
    protected boolean[] masks;
    private boolean quiet;
    protected File pwd;
    protected OutputStream stdout = NULL_OUTPUT_STREAM, stderr;
    protected InputStream stdin = new org.apache.commons.io.input.NullInputStream(0);
    protected String[] envs;
    /**
     * True to reverse the I/O direction.
     *
     * For example, if {@link #reverseStdout}==true, then we expose
     * {@link InputStream} from {@link Proc} and expect the client to read from it,
     * whereas normally we take {@link OutputStream} via {@link #stdout(OutputStream)}
     * and feed stdout into that output.
     *
     * @since 1.399
     */
    protected boolean reverseStdin, reverseStdout, reverseStderr;

    /**
     * Passes a white-space separated single-string command (like "cat abc def") and parse them
     * as a command argument. This method also handles quotes.
     */
    public ProcStarter cmdAsSingleString(String s) {
        return cmds(QuotedStringTokenizer.tokenize(s));
    }

    public ProcStarter cmds(String... args) {
        return cmds(Arrays.asList(args));
    }

    public ProcStarter cmds(File program, String... args) {
        commands = new ArrayList<String>(args.length+1);
        commands.add(program.getPath());
        commands.addAll(Arrays.asList(args));
        return this;
    }

    public ProcStarter cmds(List<String> args) {
        commands = new ArrayList<String>(args);
        return this;
    }

    public ProcStarter cmds(ArgumentListBuilder args) {
        commands = args.toList();
        masks = args.toMaskArray();
        return this;
    }

    public List<String> cmds() {
        return commands;
    }

    /**
     * Hide parts of the command line from being printed to the log.
     * @param masks true for each position in {@link #cmds(String[])} which should be masked, false to print
     * @return this
     * @see ArgumentListBuilder#add(String, boolean)
     * @see #maskedPrintCommandLine(List, boolean[], FilePath)
     */
    public ProcStarter masks(boolean... masks) {
        this.masks = masks;
        return this;
    }

    public boolean[] masks() {
        return masks;
    }

    /**
     * Allows {@link #maskedPrintCommandLine(List, boolean[], FilePath)} to be suppressed from {@link hudson.Launcher.LocalLauncher#launch(hudson.Launcher.ProcStarter)}.
     * Useful when the actual command being printed is noisy and unreadable and the caller would rather print diagnostic information in a customized way.
     * @param quiet to suppress printing the command line when starting the process; false to keep default behavior of printing
     * @return this
     * @since 1.576
     */
    public ProcStarter quiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    /**
     * @since 1.576
     */
    public boolean quiet() {
        return quiet;
    }

    public ProcStarter pwd(File workDir) {
        this.pwd = workDir;
        return this;
    }

    public ProcStarter pwd(String workDir) {
        return pwd(new File(workDir));
    }

    public File pwd() {
        return pwd;
    }

    public ProcStarter stdout(OutputStream out) {
        this.stdout = out;
        return this;
    }

    /**
     * Sends the stdout to the given {@link TaskListener}.
     */
    public ProcStarter stdout(StreamTaskListener out) {
        return stdout(out.getLogger());
    }

    public OutputStream stdout() {
        return stdout;
    }

    /**
     * Controls where the stderr of the process goes.
     * By default, it's bundled into stdout.
     */
    public ProcStarter stderr(OutputStream err) {
        this.stderr =  err;
        return this;
    }

    public OutputStream stderr() {
        return stderr;
    }

    /**
     * Controls where the stdin of the process comes from.
     * By default, <tt>/dev/null</tt>.
     */
    public ProcStarter stdin(InputStream in) {
        this.stdin = in;
        return this;
    }

    public InputStream stdin() {
        return stdin;
    }

    /**
     * Sets the environment variable overrides.
     *
     * <p>
     * In adition to what the current process
     * is inherited (if this is going to be launched from a slave agent, that
     * becomes the "current" process), these variables will be also set.
     */
    public ProcStarter envs(Map<String, String> overrides) {
        this.envs = Utils.mapToEnv(overrides);
        return this;
    }

    /**
     * @param overrides
     *      List of "VAR=VALUE". See {@link #envs(Map)} for the semantics.
     */
    public ProcStarter envs(String... overrides) {
        if (overrides != null) {
            for (String override : overrides) {
                if (override.indexOf('=') == -1) {
                    throw new IllegalArgumentException(override);
                }
            }
        }
        this.envs = overrides;
        return this;
    }

    /**
     * Gets a list of environment variables to be set.
     * Returns an empty array if envs field has not been initialized.
     * @return If initialized, returns a copy of internal envs array. Otherwise - a new empty array.
     */
    public String[] envs() {
        return envs != null ? envs.clone() : new String[0];
    }

    /**
     * Indicates that the caller will pump {@code stdout} from the child process
     * via {@link Proc#getStdout()} (whereas by default you call {@link #stdout(OutputStream)}
     * and let Jenkins pump stdout into your {@link OutputStream} of choosing.
     *
     * <p>
     * When this method is called, {@link Proc#getStdout()} will read the combined output
     * of {@code stdout/stderr} from the child process, unless {@link #readStderr()} is called
     * separately, which lets the caller read those two streams separately.
     *
     * @since 1.399
     */
    public ProcStarter readStdout() {
        reverseStdout = true;
        stdout = stderr = null;
        return this;
    }

    /**
     * In addition to the effect of {@link #readStdout()}, indicate that the caller will pump {@code stderr}
     * from the child process separately from {@code stdout}. The stderr will be readable from
     * {@link Proc#getStderr()} while {@link Proc#getStdout()} reads from stdout.
     *
     * @since 1.399
     */
    public ProcStarter readStderr() {
        reverseStdout = true;
        reverseStderr = true;
        return this;
    }

    /**
     * Indicates that the caller will directly write to the child process {@link #stdin()} via {@link Proc#getStdin()}.
     * (Whereas by default you call {@link #stdin(InputStream)}
     * and let Jenkins pump your {@link InputStream} of choosing to stdin.)
     * @since 1.399
     */
    public ProcStarter writeStdin() {
        reverseStdin = true;
        stdin = null;
        return this;
    }


    /**
     * Starts the new process as configured.
     */
    public LocalProc start() throws IOException {
    	TreeMap<String,String> jobEnv = inherit(this.envs);
        
        String[] jobCmd = this.commands.toArray(new String[]{});
        
		return new LocalProc(jobCmd, Utils.mapToEnv(jobEnv), this.reverseStdin ? LocalProc.SELFPUMP_INPUT : this.stdin,
				this.reverseStdout ? LocalProc.SELFPUMP_OUTPUT : this.stdout, this.reverseStderr ? LocalProc.SELFPUMP_OUTPUT : this.stderr,
				this.pwd);
    }
    
    /**
     * Expands the list of environment variables by inheriting current env variables.
     */
	private static TreeMap<String, String> inherit(String[] env) {
		// convert String[] to Map first
		TreeMap<String, String> m = new TreeMap<String, String>();
		if (env != null) {
			for (String e : env) {
				int index = e.indexOf('=');
				m.put(e.substring(0, index), e.substring(index + 1));
			}
		}
		// then do the inheritance
		return m;
	}


    /**
     * Starts the process and waits for its completion.
     */
    public int join() throws IOException, InterruptedException {
        return start().join();
    }

    /**
     * Copies a {@link ProcStarter}.
     */
    public ProcStarter copy() {
        ProcStarter rhs = new ProcStarter().cmds(commands).pwd(pwd).masks(masks).stdin(stdin).stdout(stdout).stderr(stderr).envs(envs).quiet(quiet);
        rhs.reverseStdin  = this.reverseStdin;
        rhs.reverseStderr = this.reverseStderr;
        rhs.reverseStdout = this.reverseStdout;
        return rhs;
    }

}
