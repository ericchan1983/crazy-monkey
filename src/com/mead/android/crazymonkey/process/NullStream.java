package com.mead.android.crazymonkey.process;

import java.io.OutputStream;

public class NullStream extends OutputStream {

	public NullStream() {
	}

	@Override
	public void write(byte b[]) {
	}

	@Override
	public void write(byte b[], int off, int len) {
	}

	public void write(int b) {
	}

}
