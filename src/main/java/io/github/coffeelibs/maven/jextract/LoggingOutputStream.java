package io.github.coffeelibs.maven.jextract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

public class LoggingOutputStream extends OutputStream {

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final Consumer<String> logger;
	private final Charset charset;

	public LoggingOutputStream(Consumer<String> logger, Charset charset) {
		this.logger = logger;
		this.charset = charset;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		int begin = off;
		for (int i = off; i < off + len; i++) {
			if (b[i] == '\n') {
				buffer.write(b, begin, i - begin);
				flush();
				begin = i + 1;
			}
		}
		// append remaining
		int delta = begin - off;
		buffer.write(b, begin, len - delta);
	}

	@Override
	public void write(int b) throws IOException {
		write(new byte[]{(byte) b});
	}

	@Override
	public void flush() {
		var line = buffer.toString(charset);
		logger.accept(line);
		buffer.reset();
	}

	@Override
	public void close() {
		if (buffer.size() > 0) {
			flush();
		}
	}
}
