import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOFunctions {
	public static void copy(InputStream in, OutputStream out, long skip)
			throws IOException {
		byte[] buf = new byte[100 * 1024];
		int len = 0;
		in.skip(skip);
		len = in.read(buf);
		out.write(buf, 0, len);
		in.skip(-1 * (skip + len));
	}
}
