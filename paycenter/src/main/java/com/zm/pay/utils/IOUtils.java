package com.zm.pay.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L)
			return -1;
		else
			return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, byte buffer[]) throws IOException {
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}

		return count;
	}

}
