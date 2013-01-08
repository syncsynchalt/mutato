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

        assertEquals(MutoCode.NOOP, mill.get(0));
        assertEquals(MutoCode.INCR, mill.get(1));
        assertEquals(MutoCode.DECR, mill.get(2));
        assertEquals(MutoCode.NEXT, mill.get(3));
        assertEquals(MutoCode.PREV, mill.get(4));
        assertEquals(MutoCode.FFWD, mill.get(5));
        assertEquals(MutoCode.RWND, mill.get(6));
        assertEquals(MutoCode.DIE,  mill.get(7));
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
        assertEquals(MutoCode.INCR, mill.get(99));
        assertEquals(MutoCode.DECR, mill.get(0));
        assertEquals(MutoCode.NEXT, mill.get(1));
        assertEquals(MutoCode.PREV, mill.get(2));
    }
}
