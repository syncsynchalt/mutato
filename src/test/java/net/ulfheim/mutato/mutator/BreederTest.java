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
    public void testMutations()
    {
        String code =  ".......";
        String cross = "-------";
        for (int i = 0; i < 1000; i++)
        {
            String newCode = Breeder.mutate(code, cross);
            assertTrue(newCode, code.equals(newCode) == false);
            System.out.println(i + " " + newCode);
        }
    }

}
