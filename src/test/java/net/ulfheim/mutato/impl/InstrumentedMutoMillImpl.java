package net.ulfheim.mutato.impl;

import java.util.ArrayList;
import java.util.List;
import net.ulfheim.mutato.MutoMill;

/**
 *
 * @author mdriscoll
 */
public class InstrumentedMutoMillImpl implements MutoMill
{
    private MutoMill mill;
    private List<String> instructions = new ArrayList<String>();

    InstrumentedMutoMillImpl(int size)
    {
        mill = new MutoMillImpl(size);
    }

    @Override
    public void decr(int index)
    {
        instructions.add("DECR " + index);
        mill.decr(index);
    }

    @Override
    public int get(int index)
    {
        instructions.add("GET " + index);
        return mill.get(index);
    }

    public int directGet(int index)
    {
        while (index < 0) {
            index += mill.size();
        }
        return mill.get(index % mill.size());
    }

    @Override
    public void incr(int index)
    {
        instructions.add("INCR " + index);
        mill.incr(index);
    }

    @Override
    public int size()
    {
        return mill.size();
    }

    public List<String> getInstructions()
    {
        return new ArrayList<String>(instructions);
    }
}
