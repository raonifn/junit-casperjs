package com.github.raonifn.casperjs.junit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Pipe implements Runnable {

	private final static int DEFAULT_BUFFER_SIZE = 5 * 1024 * 1024;

	private InputStream in;

	private List<OutputStream> outs;

	private byte[] buff;

	public Pipe(InputStream in, List<OutputStream> outs, byte[] buff) {
		this.in = in;
		this.outs = outs;
		this.buff = buff;
	}

	public Pipe(InputStream is, List<OutputStream> outs) {
		this.in = is;
		this.outs = outs;
		this.buff = new byte[DEFAULT_BUFFER_SIZE];
	}

	public void run() {
		int read;
		do {
			read = copy();
		} while (read >= 0);
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	private int copy() {
		try {
			int read = in.read(buff);
			if (read > 0) {
				for (OutputStream out : outs) {
					out.write(buff, 0, read);
				}
			}
			return read;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
