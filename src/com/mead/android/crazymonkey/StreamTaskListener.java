package com.mead.android.crazymonkey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamTaskListener {
	private PrintStream out;
	private Charset charset;

	public StreamTaskListener(OutputStream out) {
		this(out, null);
	}

	public StreamTaskListener(OutputStream out, Charset charset) {
		try {
			if (charset == null)
				this.out = (out instanceof PrintStream) ? (PrintStream) out : new PrintStream(out, false);
			else
				this.out = new PrintStream(out, false, charset.name());
			this.charset = charset;
		} catch (UnsupportedEncodingException e) {
			// it's not very pretty to do this, but otherwise we'd have to touch too many call sites.
			throw new Error(e);
		}
	}

	public StreamTaskListener(File out) throws IOException {
		this(out, null);
	}

	public StreamTaskListener(File out, Charset charset) throws IOException {
		// don't do buffering so that what's written to the listener
		// gets reflected to the file immediately, which can then be
		// served to the browser immediately
		this(new FileOutputStream(out), charset);
	}

	public static StreamTaskListener fromStdout() {
		return new StreamTaskListener(System.out, Charset.defaultCharset());
	}

	public static StreamTaskListener fromStderr() {
		return new StreamTaskListener(System.err, Charset.defaultCharset());
	}

	private PrintWriter _error(String prefix, String msg) {
		out.print(prefix);
		out.println(msg);

		return new PrintWriter(charset != null ? new OutputStreamWriter(out, charset) : new OutputStreamWriter(out), true);
	}

	public PrintWriter error(String msg) {
		return _error("ERROR: ", msg);
	}

	public PrintWriter error(String format, Object... args) {
		return error(String.format(format, args));
	}

	public PrintWriter fatalError(String msg) {
		return _error("FATAL: ", msg);
	}

	public PrintWriter fatalError(String format, Object... args) {
		return fatalError(String.format(format, args));
	}

	public void close() throws IOException {
		out.close();
	}

	public void closeQuietly() {
		try {
			close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed to close", e);
		}
	}

	private static final Logger LOGGER = Logger.getLogger(StreamTaskListener.class.getName());

	public PrintStream getLogger() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

}
