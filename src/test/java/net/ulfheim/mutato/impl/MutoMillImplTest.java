package net.ulfheim.mutato.impl;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class MutoMillImplTest
{
    /**
     * Test of size method, of class MutoMill.
     */
    @Test
    public void testSize()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        assertEquals(128, mill.size());
    }

    /**
     * Test of get method, of class MutoMill.
     */
    @Test
    public void testGet()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        assertEquals(0, mill.get(0));
        assertEquals(0, mill.get(127));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetUnder()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        assertEquals(0, mill.get(-1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetOver()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        assertEquals(0, mill.get(128));
    }

    @Test
    public void testIncr()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        mill.incr(100);
        assertEquals(0, mill.get(99));
        assertEquals(1, mill.get(100));
        assertEquals(0, mill.get(101));
    }

    @Test
    public void testIncrDecr()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        mill.incr(100);
        mill.decr(100);
        assertEquals(0, mill.get(100));
    }

    @Test
    public void testIncrWrap()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        mill.incr(1);
        mill.incr(1);
        mill.incr(1);
        mill.incr(1);
        assertEquals(4, mill.get(1));
        mill.incr(1);
        mill.incr(1);
        mill.incr(1);
        mill.incr(1);
        assertEquals(0, mill.get(1));
    }

    @Test
    public void testDecrWrap()
    {
        MutoMillImpl mill = new MutoMillImpl(128);
        mill.decr(0);
        assertEquals(7, mill.get(0));
        mill.incr(0);
        assertEquals(0, mill.get(0));
    }
}
