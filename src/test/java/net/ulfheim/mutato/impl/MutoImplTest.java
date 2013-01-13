package net.ulfheim.mutato.impl;

import java.util.List;
import net.ulfheim.mutato.MutoMill;
import org.junit.Test;
import net.ulfheim.mutato.Compiler;
import net.ulfheim.mutato.Muto;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class MutoImplTest
{
    MutoMill codeMill = new MutoMillImpl(256);
    InstrumentedMutoMillImpl dataMill = new InstrumentedMutoMillImpl(256);

    private Muto prepTestMuto(String code) throws Exception
    {
        Compiler.translateProgram(codeMill, code, 0);
        return new MutoImpl(codeMill, dataMill, 0, code.length(), 1);
    }

    private void runSteps(Muto muto, int count)
    {
        for (int i = 0; i < count; i++)
        {
            assertFalse(muto.step());
        }
        printMillInstructions();
    }

    private void assertEqualsRange(int start, int end, int value)
    {
        for (int i = start; i < end; i++)
        {
            assertEquals("index " + i, value, dataMill.directGet(i));
        }
    }

    private void printMillInstructions()
    {
        List<String> instructions = dataMill.getInstructions();
        for (String instruction : instructions)
        {
            System.out.println("INSTRUCTION: " + instruction);
        }
    }

    @Test
    public void testImp() throws Exception
    {
        System.out.println("imp");
        String code = "+>";
        Muto muto = prepTestMuto(code);
        runSteps(muto, 10);
        assertEqualsRange(0, 4, 1);
    }

    @Test
    public void testHopper() throws Exception
    {
        System.out.println("hopper");
        String code = "-^<";
        Muto muto = prepTestMuto(code);
        runSteps(muto, 10);
        assertEquals(7, dataMill.directGet(0));
        assertEquals(7, dataMill.directGet(7*16-1));
        assertEquals(7, dataMill.directGet(7*16-1 + 7*16-1));
    }

    @Test
    public void testWrapAddr() throws Exception
    {
        System.out.println("wrapAddr");
        String code = "-^-^-^-";
        Muto muto = prepTestMuto(code);
        runSteps(muto, 7);
        assertEquals(7, dataMill.directGet(0));
        assertEquals(7, dataMill.directGet(7*16));
        assertEquals(7, dataMill.directGet(7*16 + 7*16));
        assertEquals(7, dataMill.directGet(7*16 + 7*16 + 7*16));
    }

    @Test
    public void testWrapCell() throws Exception
    {
        System.out.println("wrapCell");
        String code = "----------";
        Muto muto = prepTestMuto(code);
        runSteps(muto, 10);
        assertEquals(6, dataMill.directGet(0));
        assertEqualsRange(1, 256, 0);
    }

    @Test
    public void testRewind() throws Exception
    {
        System.out.println("rewind");
        String code = "-v+";
        Muto muto = prepTestMuto(code);
        runSteps(muto, 3);
        assertEquals(7, dataMill.directGet(0));
        assertEquals(1, dataMill.directGet(-7*16));
    }
}
