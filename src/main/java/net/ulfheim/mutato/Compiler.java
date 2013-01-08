package net.ulfheim.mutato;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import net.ulfheim.mutato.exception.CompilerException;

/**
 * Reads muto code, and translates it into a mill.
 * @author mdriscoll
 */
public class Compiler
{
    public static void translateProgram(MutoMill mill, String code, int offset)
            throws CompilerException
    {
        int millSize = mill.size();
        if (code.length() > millSize)
            throw new CompilerException("size of mill exceeded");

        for (int i = 0; i < code.length(); i++)
        {
            int index = wrapOffset(offset + i, millSize);
            int c = MutoCode.valueOf(code.charAt(i));
            for (int j = 0; j < c; j++)
            {
                mill.incr(index);
            }
        }
    }

    public static String readProgram(String filename)
            throws FileNotFoundException, IOException
	{
		FileInputStream stream = new FileInputStream(filename);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			String source = Charset.forName("utf-8").decode(bb).toString();
            source = source.replaceAll("\\s", "");
            return source;
		} finally {
			stream.close();
		}
	}

    private static int wrapOffset(int offset, int size)
    {
        if (offset >= size)
            offset -= size;
        return offset;
    }
}
