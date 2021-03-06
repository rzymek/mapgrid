package pl.maps.tms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class StreamUtils {
	public static void copy(InputStream input, OutputStream output) throws IOException {
		// get an channel from the stream
		ReadableByteChannel inputChannel = Channels.newChannel(input);
		WritableByteChannel outputChannel = Channels.newChannel(output);
		// copy the channels
		copy(inputChannel, outputChannel);
		// closing the channels
		inputChannel.close();
		outputChannel.close();		
	}
	
	public static void copy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}
}
