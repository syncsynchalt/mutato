package net.ulfheim.mutato.mutator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class BreederTest
{
    /**
     * Test of mutate method, of class Breeder.
     */
    @Test
    public void testShowMutations()
    {
        String code =  ".......";
        String cross = "-------";
        for (int i = 0; i < 20; i++)
        {
            String newCode = Breeder.mutate(code, cross);
            assertTrue(newCode, code.equals(newCode) == false);
            System.out.println(i + " " + newCode);
        }
    }

    /**
     * Test of mutate method, of class Breeder.
     */
    @Test
    public void testMultipleMutations()
    {
        String startCode = "............";
        String code = startCode;
        String cross = "";
        for (int i = 0; i < 2048; i++)
        {
            code = Breeder.mutate(code, cross);
        }
        System.out.println("final code is " + code);
        System.out.println("length has gone from " + startCode.length() + " to " + code.length());;
    }

}
