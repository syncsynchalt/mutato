package net.ulfheim.mutato;

import net.ulfheim.mutato.exception.CompilerException;
import net.ulfheim.mutato.impl.MutoMillImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class CompilerTest
{
    
    /**
     * Test of translateProgram method, of class Compiler.
     */
    @Test
    public void testTranslateProgram() throws Exception
    {
        MutoMill mill = new MutoMillImpl(100);
        String code = ".+-><^vx";
        Compiler.translateProgram(mill, code, 0);

        for (int i = 0; i < 8; ++i)
        {
            assertEquals(i, mill.get(i));
        }
        assertEquals(0, mill.get(8));
    }

    @Test(expected = CompilerException.class)
    public void testProgramSize() throws Exception
    {
        MutoMill mill = new MutoMillImpl(2);
        String code = "...";
        Compiler.translateProgram(mill, code, 0);
    }

    @Test
    public void testProgramWraps() throws Exception
    {
        MutoMill mill = new MutoMillImpl(100);
        String code = "+-><";
        Compiler.translateProgram(mill, code, 99);
        assertEquals(1, mill.get(99));
        assertEquals(2, mill.get(0));
        assertEquals(3, mill.get(1));
        assertEquals(4, mill.get(2));
    }
}
