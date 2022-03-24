package io.github.coffeelibs.maven.jextract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class LoggingOutputStreamTest {

	private Consumer<String> logger;
	private LoggingOutputStream out;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setup() {
		this.logger = Mockito.mock(Consumer.class);
		this.out = new LoggingOutputStream(logger, StandardCharsets.UTF_8);
	}

	@Test
	@DisplayName("don't log if nothing got written")
	public void testNoWriteNoLog() throws IOException {
		out.write(new byte[0]);
		out.close();

		Mockito.verifyNoMoreInteractions(logger);
	}

	@Test
	@DisplayName("write just newlines")
	public void testJustNewlines() throws IOException {
		out.write('\n');
		out.write('\n');
		out.write('\n');
		out.close();

		Mockito.verify(logger, Mockito.times(3)).accept("");
	}

	@Test
	@DisplayName("write with no newlines")
	public void testWriteNoNewlines() throws IOException {
		out.write("foo".getBytes(StandardCharsets.UTF_8));

		Mockito.verifyNoInteractions(logger);
	}

	@Test
	@DisplayName("write with two newlines")
	public void testWriteTwoNewlines() throws IOException {
		out.write("foo\nbar\nbaz".getBytes(StandardCharsets.UTF_8));

		Mockito.verify(logger).accept("foo");
		Mockito.verify(logger).accept("bar");
		Mockito.verifyNoMoreInteractions(logger);
	}

	@Test
	@DisplayName("multiple writes followed by newline")
	public void testMultipleWriteBeforeNewline() throws IOException {
		out.write('f');
		out.write('o');
		out.write('o');
		out.write("bar".getBytes(StandardCharsets.UTF_8));
		out.write("\nbaz".getBytes(StandardCharsets.UTF_8));

		Mockito.verify(logger).accept("foobar");
		Mockito.verifyNoMoreInteractions(logger);
	}

	@Test
	@DisplayName("write with consecutive newlines")
	public void testConsecutiveNewlines() throws IOException {
		out.write("foo\n\nbar\nbaz".getBytes(StandardCharsets.UTF_8));

		Mockito.verify(logger).accept("foo");
		Mockito.verify(logger).accept("");
		Mockito.verify(logger).accept("bar");
		Mockito.verifyNoMoreInteractions(logger);
	}

	@Test
	@DisplayName("flush on close")
	public void testFlushOnClose() throws IOException {
		out.write("foo\nbar\nbaz".getBytes(StandardCharsets.UTF_8));
		out.close();

		Mockito.verify(logger).accept("foo");
		Mockito.verify(logger).accept("bar");
		Mockito.verify(logger).accept("baz");
		Mockito.verifyNoMoreInteractions(logger);
	}

}