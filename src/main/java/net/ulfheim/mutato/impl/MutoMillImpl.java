package net.ulfheim.mutato.impl;

import net.ulfheim.mutato.MutoMill;

/**
 * Set of 3-bit values that make up a both a {@link Muto}'s instruction set and
 * data store.  Valid values in the Mill are 0-7 inclusive.  Values are set to
 * 0 on initialization.
 *
 * @author mdriscoll
 */
public class MutoMillImpl implements MutoMill
{
    byte[] mill;

    public MutoMillImpl(int size) 
    {
        mill = new byte[size];
    }

    @Override
    public int size()
    {
        return mill.length;
    }

    @Override
    public int get(int index)
    {
        return mill[index];
    }

    @Override
    public void decr(int index)
    {
        if (mill[index] == 0)
            mill[index] = 7;
        else
            mill[index]--;
    }

    @Override
    public void incr(int index)
    {
        if (mill[index] == 7)
            mill[index] = 0;
        else
            mill[index]++;
    }
}
