package com.mead.android.crazymonkey.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mead.android.crazymonkey.AndroidEmulator;
import com.mead.android.crazymonkey.CrazyMonkeyBuild;
import com.mead.android.crazymonkey.Messages;
import com.mead.android.crazymonkey.process.ArgumentListBuilder;
import com.mead.android.crazymonkey.process.Callable;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.process.ProcStarter;
import com.mead.android.crazymonkey.process.QuotedStringTokenizer;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.SdkInstallationException;
import com.mead.android.crazymonkey.sdk.Tool;

public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	private static final Pattern REVISION = Pattern.compile("(\\d++).*");

	public static File getHomeDirectory(String androidSdkHome) {
		String homeDirPath = System.getenv("ANDROID_SDK_HOME");
		if (homeDirPath == null) {
			if (androidSdkHome != null) {
				homeDirPath = androidSdkHome;
			} else if (!isWindows()) {
				homeDirPath = System.getenv("HOME");
				if (homeDirPath == null) {
					homeDirPath = "/tmp";
				}
			} else {
				homeDirPath = System.getenv("USERPROFILE");
				if (homeDirPath == null) {
					homeDirPath = System.getProperty("user.home");
				}
			}
		}

		return new File(homeDirPath);
	}

	public static File getCrazyMonkeyHomeDirectory(String crazyMonkeyHome) {
		String homeDirPath = System.getenv("CRAZY_MONKEY_HOME");

		if (homeDirPath == null) {
			if (crazyMonkeyHome != null) {
				homeDirPath = crazyMonkeyHome;
			} else if (!isWindows()) {
				homeDirPath = System.getenv("HOME");
				if (homeDirPath == null) {
					homeDirPath = "/tmp";
				}
			} else {
				homeDirPath = System.getenv("USERPROFILE");
				if (homeDirPath == null) {
					homeDirPath = System.getProperty("user.home");
				}
			}
		}
		return new File(homeDirPath);
	}

	public static boolean isWindows() {
		return File.pathSeparatorChar == ';';
	}

	public static boolean isUnix() {
		return File.pathSeparatorChar == ':';
	}

	/**
	 * Parses the contents of a properties file into a map.
	 * 
	 * @param configFile The file to read.
	 * @return The key-value pairs contained in the file, ignoring any comments or blank lines.
	 * @throws IOException If the file could not be read.
	 */
	public static Map<String, String> parseConfigFile(File configFile) throws IOException {
		FileReader fileReader = new FileReader(configFile);
		BufferedReader reader = new BufferedReader(fileReader);
		Properties properties = new Properties();
		properties.load(reader);
		reader.close();

		final Map<String, String> values = new HashMap<String, String>();
		for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
			values.put((String) entry.getKey(), (String) entry.getValue());
		}

		return values;
	}

	public static void writeAvdConfigFile(File homeDir, Map<String, String> values, String avdName) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();

		for (String key : values.keySet()) {
			sb.append(key);
			sb.append("=");
			sb.append(values.get(key));
			sb.append("\r\n");
		}

		File configFile = new File(getAvdDirectory(homeDir, avdName), "config.ini");
		PrintWriter out = new PrintWriter(configFile);
		out.print(sb.toString());
		out.flush();
		out.close();
	}

	public static String fixNull(String s) {
		if (s == null)
			return "";
		else
			return s;
	}

	/**
	 * Determines the relative path required to get from one path to another.
	 * 
	 * @param from Path to go from.
	 * @param to Path to reach.
	 * @return The relative path between the two, or {@code null} for invalid input.
	 */
	public static String getRelativePath(String from, String to) {
		// Check for bad input
		if (from == null || to == null) {
			return null;
		}

		String fromPath, toPath;
		try {
			fromPath = new File(from).getCanonicalPath();
			toPath = new File(to).getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		// Nothing to do if the two are equal
		if (fromPath.equals(toPath)) {
			return "";
		}
		// Target directory is a subdirectory
		if (toPath.startsWith(fromPath)) {
			int fromLength = fromPath.length();
			int index = fromLength == 1 ? 1 : fromLength + 1;
			return toPath.substring(index) + File.separatorChar;
		}

		// Quote separator, as String.split() takes a regex and
		// File.separator isn't a valid regex character on Windows
		final String separator = Pattern.quote(File.separator);
		// Target directory is somewhere above our directory
		String[] fromParts = fromPath.substring(1).split(separator);
		final int fromLength = fromParts.length;
		String[] toParts = toPath.substring(1).split(separator);
		final int toLength = toParts.length;

		// Find the number of common path segments
		int commonLength = 0;
		for (int i = 0; i < toLength; i++) {
			if (fromParts[i].length() == 0) {
				continue;
			}
			if (!fromParts[i].equals(toParts[i])) {
				break;
			}
			commonLength++;
		}

		// Determine how many directories up we need to go
		int diff = fromLength - commonLength;
		StringBuilder rel = new StringBuilder();
		for (int i = 0; i < diff; i++) {
			rel.append("..");
			rel.append(File.separatorChar);
		}

		// Add on the remaining path segments to the target
		for (int i = commonLength; i < toLength; i++) {
			rel.append(toParts[i]);
			rel.append(File.separatorChar);
		}

		return rel.toString();
	}

	/**
	 * Determines the number of steps required to get between two paths.
	 * <p/>
	 * e.g. To get from "/foo/bar/baz" to "/foo/blah" requires making three steps:
	 * <ul>
	 * <li>"/foo/bar"</li>
	 * <li>"/foo"</li>
	 * <li>"/foo/blah"</li>
	 * </ul>
	 * 
	 * @param from Path to go from.
	 * @param to Path to reach.
	 * @return The relative distance between the two, or {@code -1} for invalid input.
	 */
	public static int getRelativePathDistance(String from, String to) {
		final String relative = getRelativePath(from, to);
		if (relative == null) {
			return -1;
		}

		final String[] parts = relative.split("/");
		final int length = parts.length;
		if (length == 1 && parts[0].isEmpty()) {
			return 0;
		}
		return parts.length;
	}

	/**
	 * Attempts to parse an SDK revision string.
	 * 
	 * @param revisionStr Version string.
	 * @return The major version (i.e. "26.0.2" returns 26).
	 * @throws java.lang.NumberFormatException If no number could be determined.
	 */
	public static int parseRevisionString(String revisionStr) {
		try {
			return Integer.parseInt(revisionStr);
		} catch (NumberFormatException e) {
			Matcher matcher = REVISION.matcher(revisionStr);
			if (matcher.matches()) {
				return Integer.parseInt(matcher.group(1));
			}
			throw new NumberFormatException("Could not parse " + revisionStr);
		}
	}

	public static String fixEmptyAndTrim(String s) {
		if (s == null || s.length() == 0)
			return null;
		return s.trim();
	}

	/**
	 * Determines the API level for the given platform name.
	 * 
	 * @param platform String like "android-4" or "Google:Google APIs:14".
	 * @return The detected version, or {@code -1} if not determined.
	 */
	public static int getApiLevelFromPlatform(String platform) {
		int apiLevel = -1;
		platform = fixEmptyAndTrim(platform);
		if (platform == null) {
			return apiLevel;
		}

		Matcher matcher = Pattern.compile("[-:]([0-9]{1,2})$").matcher(platform);
		if (matcher.find()) {
			String end = matcher.group(1);
			try {
				apiLevel = Integer.parseInt(end);
			} catch (NumberFormatException e) {
			}
		}
		return apiLevel;
	}

	public static File getAvdConfigFile(File homeDir, String name) {
		return new File(getAvdDirectory(homeDir, name), "config.ini");
	}

	public static File getAvdDirectory(final File homeDir, String name) {
		return new File(getAvdHome(homeDir), name + ".avd");
	}

	public static File getAvdHome(final File homeDir) {
		return new File(homeDir, ".android/avd/");
	}

	/**
	 * Generates a ready-to-use ArgumentListBuilder for one of the Android SDK tools.
	 * 
	 * @param androidSdk The Android SDK to use.
	 * @param isUnix Whether the system where this command should run is sane.
	 * @param tool The Android tool to run.
	 * @param args Any extra arguments for the command.
	 * @return Arguments including the full path to the SDK and any extra Windows stuff required.
	 */
	public static ArgumentListBuilder getToolCommand(AndroidSdk androidSdk, boolean isUnix, Tool tool, String args) {
		// Determine the path to the desired tool
		String androidToolsDir;
		if (androidSdk.hasKnownRoot()) {
			try {
				androidToolsDir = androidSdk.getSdkRoot() + tool.findInSdk(androidSdk);
			} catch (SdkInstallationException e) {
				LOGGER.warning("A build-tools directory was found but there were no build-tools installed. Assuming command is on the PATH");
				androidToolsDir = "";
			}
		} else {
			LOGGER.warning("SDK root not found. Assuming command is on the PATH");
			androidToolsDir = "";
		}

		// Build tool command
		final String executable = tool.getExecutable(isUnix);
		ArgumentListBuilder builder = new ArgumentListBuilder(androidToolsDir + executable);
		if (args != null) {
			builder.add(tokenize(args));
		}

		return builder;
	}
	
	/**
	 * Generates a ready-to-use ArgumentListBuilder for one of the Android SDK tools.
	 * 
	 * @param androidSdk The Android SDK to use.
	 * @param isUnix Whether the system where this command should run is sane.
	 * @param tool The Android tool to run.
	 * @param args Any extra arguments for the command.
	 * @return Arguments including the full path to the SDK and any extra Windows stuff required.
	 */
	public static ArgumentListBuilder getToolCommand(AndroidSdk androidSdk, boolean isUnix, Tool tool, String program, String args) {
		// Determine the path to the desired tool
		String androidToolsDir;
		if (androidSdk.hasKnownRoot()) {
			try {
				androidToolsDir = androidSdk.getSdkRoot() + tool.findInSdk(androidSdk);
			} catch (SdkInstallationException e) {
				LOGGER.warning("A build-tools directory was found but there were no build-tools installed. Assuming command is on the PATH");
				androidToolsDir = "";
			}
		} else {
			LOGGER.warning("SDK root not found. Assuming command is on the PATH");
			androidToolsDir = "";
		}

		// Build tool command
		// final String executable = tool.getExecutable(isUnix);
		ArgumentListBuilder builder = new ArgumentListBuilder(androidToolsDir + program);
		if (args != null) {
			builder.add(tokenize(args));
		}

		return builder;
	}

	public static String[] tokenize(String s, String delimiter) {
		return QuotedStringTokenizer.tokenize(s, delimiter);
	}

	public static String[] tokenize(String s) {
		return tokenize(s, " \t\n\r\f");
	}

	public static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len;
		while ((len = in.read(buf)) >= 0)
			out.write(buf, 0, len);
	}

	public static void copyStream(Reader in, Writer out) throws IOException {
		char[] buf = new char[8192];
		int len;
		while ((len = in.read(buf)) > 0)
			out.write(buf, 0, len);
	}

	public static void copyStreamAndClose(InputStream in, OutputStream out) throws IOException {
		try {
			copyStream(in, out);
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(in);
			org.apache.commons.io.IOUtils.closeQuietly(out);
		}
	}

	public static Map<String, String> parseAvdConfigFile(File homeDir, String avdName) throws IOException {
		File configFile = getAvdConfigFile(homeDir, avdName);
		return Utils.parseConfigFile(configFile);
	}

	@SuppressWarnings("unused")
	public static void copyFile(File oldfile, File newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				@SuppressWarnings("resource")
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyStreamAndClose(Reader in, Writer out) throws IOException {
		try {
			copyStream(in, out);
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(in);
			org.apache.commons.io.IOUtils.closeQuietly(out);
		}
	}

	public static void delFile(File f) throws IOException {
		if (f.exists() && f.isDirectory()) {
			if (f.listFiles().length == 0) {
				f.delete();
			} else {
				File delFile[] = f.listFiles();
				for (int j = 0; j < f.listFiles().length; j++) {
					if (delFile[j].isDirectory()) {
						delFile(delFile[j]);
					}
					delFile[j].delete();
				}
			}
		} else {
			f.delete();
		}
	}

	public static File getAvdMetadataFile(String androidSdkHome, String avdName) {
		final File homeDir = Utils.getHomeDirectory(androidSdkHome);
		return new File(getAvdHome(homeDir), avdName + ".ini");
	}

	public static String[] mapToEnv(Map<String,String> m) {
        String[] r = new String[m.size()];
        int idx=0;

        for (final Map.Entry<String,String> e : m.entrySet()) {
            r[idx++] = e.getKey() + '=' + e.getValue();
        }
        return r;
    }

	/**
     * Attempts to kill the given process, timing-out after {@code timeoutMs}.
     *
     * @param process The process to kill.
     * @param timeoutMs How long to wait for before cancelling the attempt to kill the process.
     * @return {@code true} if the process was killed successfully.
     */
    public static boolean killProcess(final LocalProc process, final int timeoutMs) {
        Boolean result = null;
        FutureTask<Boolean> task = null;
        try {
            // Attempt to kill the process; remoting will be handled by the process object
            task = new FutureTask<Boolean>(new java.util.concurrent.Callable<Boolean>() {
                public Boolean call() throws Exception {
                    process.kill();
                    return true;
                }
            });

            // Execute the task asynchronously and wait for a result or timeout
            Executors.newSingleThreadExecutor().execute(task);
            result = task.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // Ignore
        } finally {
            if (task != null && !task.isDone()) {
                task.cancel(true);
            }
        }

        return Boolean.TRUE.equals(result);
    }
    
	/** Task that will execute a command on the given emulator's console port, then quit. */
    private static final class EmulatorCommandTask implements Callable<Boolean, IOException> {

        private final int port;
        private final String command;

        EmulatorCommandTask(int port, String command) {
            this.port = port;
            this.command = command;
        }

        public Boolean call() throws IOException {
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                // Connect to the emulator's console port
                socket = new Socket("127.0.0.1", port);
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // If we didn't get a banner response, give up
                if (in.readLine() == null) {
                    return false;
                }

                // Send command, then exit the console
                out.write(command);
                out.write("\r\n");
                out.flush();
                out.write("quit\r\n");
                out.flush();

                // Wait for the commands to return a response
                while (in.readLine() != null) {
                    // Ignore
                }
            } finally {
                try {
                    out.close();
                } catch (Exception ignore) {}
                try {
                    in.close();
                } catch (Exception ignore) {}
                try {
                    socket.close();
                } catch (Exception ignore) {}
            }

            return true;
        }

        private static final long serialVersionUID = 1L;
    }

	public static void runAndroidTool(OutputStream stdout, OutputStream stderr, AndroidSdk androidSdk, Tool tool, String args,
			File workingDirectory) throws IOException, InterruptedException {

		ArgumentListBuilder cmd = Utils.getToolCommand(androidSdk, isUnix(), tool, args);

		ProcStarter procStarter = new ProcStarter().stdout(stdout).stderr(stderr).cmds(cmd);

		// Copy the old one, so we don't mutate the argument.
		Map<String, String> env = new TreeMap<String, String>();

		if (androidSdk.hasKnownHome()) {
			env.put("ANDROID_SDK_HOME", androidSdk.getSdkHome());
		}

		if (env != null) {
			procStarter = procStarter.envs(env);
		}

		if (workingDirectory != null) {
			procStarter.pwd(workingDirectory);
		}
		procStarter.join();
	}
	
	/**
     * Sends a user command to the running emulator via its telnet interface.<br>
     * Execution will be cancelled if it takes longer than {@code timeoutMs}.
     *
     * @param logger The build logger.
     * @param launcher The launcher for the remote node.
     * @param port The emulator's telnet port.
     * @param command The command to execute on the emulator's telnet interface.
     * @param timeoutMs How long to wait (in ms) for the command to complete before cancelling it.
     * @return Whether sending the command succeeded.
     */
    public static boolean sendEmulatorCommand(CrazyMonkeyBuild build, final PrintStream logger,
            final int port, final String command, int timeoutMs) {
        Boolean result = null;
        Future<Boolean> future = null;
        try {
            // Execute the task on the remote machine asynchronously, with a timeout
            EmulatorCommandTask task = new EmulatorCommandTask(port, command);
            future = build.getChannel().callAsync(task);
            
            result = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            
        } catch (IOException e) {
            // Slave communication failed
            AndroidEmulator.log(logger, Messages.SENDING_COMMAND_FAILED(command, e));
            e.printStackTrace(logger);
        } catch (InterruptedException e) {
            // Ignore; the caller should handle shutdown
        } catch (ExecutionException e) {
            // Exception thrown while trying to execute command
            if (command.equals("kill") && e.getCause() instanceof SocketException) {
                // This is expected: sending "kill" causes the emulator process to kill itself
                result = true;
            } else {
                // Otherwise, it was some generic failure
            	AndroidEmulator.log(logger, Messages.SENDING_COMMAND_FAILED(command, e));
                e.printStackTrace(logger);
            }
        } catch (TimeoutException e) {
            // Command execution timed-out
        	AndroidEmulator.log(logger, Messages.SENDING_COMMAND_TIMED_OUT(command));
        } finally {
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
        }

        return Boolean.TRUE.equals(result);
    }
    
    
	public static String getMACAddr() {
		try {
			String firstInterface = null;
			Map<String, String> addressByNetwork = new HashMap<>();
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface network = networkInterfaces.nextElement();

				byte[] bmac = network.getHardwareAddress();
				if (bmac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < bmac.length; i++) {
						sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
					}

					if (sb.toString().isEmpty() == false) {
						addressByNetwork.put(network.getName(), sb.toString());
						//System.out.println("Address = " + sb.toString() + " @ [" + network.getName() + "] " + network.getDisplayName());
					}

					if (sb.toString().isEmpty() == false && firstInterface == null) {
						firstInterface = network.getName();
					}
				}
			}
			if (firstInterface != null) {
				return addressByNetwork.get(firstInterface);
			}
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
	}
}
