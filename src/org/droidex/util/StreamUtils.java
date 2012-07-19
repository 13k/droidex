package org.droidex.util;

import java.io.InputStream;
import java.io.IOException;

public class StreamUtils
{
    public static byte[] readBytes(InputStream in)
        throws IOException
    {
        byte[] bytes = new byte[0];
        byte[] buf = new byte[1024 * 512];

        while (true) {
            int count = in.read(buf);
            if (count == -1)
                break;
            if (count < buf.length) {
                byte[] shorterBuf = new byte[count];
                System.arraycopy(buf, 0, shorterBuf, 0, count);
                buf = shorterBuf;
            }
            bytes = ArrayUtils.concat(bytes, buf);
        }

        return bytes;
    }
}
