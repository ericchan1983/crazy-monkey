package com.mead.android.crazymonkey.process;

import java.io.IOException;
import java.io.OutputStream;

public class ForkOutputStream extends OutputStream {
	

    private final OutputStream lhs;
    private final OutputStream rhs;

    public ForkOutputStream(OutputStream lhs, OutputStream rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public void write(int b) throws IOException {
        lhs.write(b);
        rhs.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        lhs.write(b);
        rhs.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        lhs.write(b, off, len);
        rhs.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        lhs.flush();
        rhs.flush();
    }

    @Override
    public void close() throws IOException {
        lhs.close();
        rhs.close();
    }


}
