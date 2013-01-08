package net.ulfheim.mutato.impl;

import net.ulfheim.mutato.Muto;
import net.ulfheim.mutato.MutoCode;
import net.ulfheim.mutato.MutoMill;
import org.apache.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
public class MutoImpl implements Muto
{
    private static final Logger logger = Logger.getLogger(MutoImpl.class.getName());

    private final MutoMill code;
    private final int codeSize;
    private final MutoMill data;
    private final int dataSize;
    private final int sixteenth;

    private final int codeStart; // where in code mill does our code loop start
    private final int codeEnd;   // where in code mill does our code loop end

    private int codePtr;
    private int dataPtr;

    /**
     * @param code mill from which we'll read our code
     * @param data mill in which we can change data
     * @param codeIndex where in code mill our code loop starts
     * @param codeLength length of code loop from codeIndex
     */
    public MutoImpl(MutoMill code, MutoMill data, int codeIndex, int codeLength)
    {
        this.code = code;
        this.codeSize = code.size();
        this.data = data;
        this.dataSize = data.size();
        this.sixteenth = (dataSize/16 == 0 ? 1 : dataSize/16);

        this.codeStart = codeIndex;
        this.codePtr = codeIndex;

        int end = codeIndex + codeLength;
        if (end > codeSize)
            end -= codeSize;
        this.codeEnd = end;
    }

    /**
     * Execute one instruction in our code mill.
     * @return whether we executed the death instruction
     */
    @Override
    public boolean step()
    {
        // loop reset costs 1 step
        if (codePtr == codeEnd)
        {
            codePtr = codeStart;
            return false;
        }

        int c = code.get(codePtr);
        int curData;
        switch (c)
        {
            case MutoCode.NOOP:
                break;
            case MutoCode.INCR:
                data.incr(dataPtr);
                break;
            case MutoCode.DECR:
                data.decr(dataPtr);
                break;
            case MutoCode.NEXT:
                modifyDataPtr(1);
                break;
            case MutoCode.PREV:
                modifyDataPtr(-1);
                break;
            case MutoCode.FFWD:
                curData = data.get(dataPtr);
                modifyDataPtr(curData*sixteenth);
                break;
            case MutoCode.RWND:
                curData = data.get(dataPtr);
                modifyDataPtr(-curData*sixteenth);
                break;
            case MutoCode.DIE:
                return true;
            default:
                throw new IllegalStateException(
                        "Got code " + c + " from codePtr " + codePtr + " somehow");
        }
        incrCodePtr();
        return false;
    }

    public void logState()
    {
        logger.debug(" muto:" + this + " codePtr:" + codePtr
                + " code:" + code.get(codePtr) + " dataPtr:" + dataPtr
                + " data:" + data.get(dataPtr));
    }

    /**
     * Increment the instruction pointer, wrapping around code mill size and 
     * our code loop length.
     */
    private void incrCodePtr()
    {
        codePtr++;
        if (codePtr >= codeSize)
            codePtr -= codeSize;
    }

    /**
     * Modify the dataPtr by amount.
     * @param amount change increment, positive or negative.
     */
    private void modifyDataPtr(int amount)
    {
        dataPtr += amount;
        while (dataPtr < 0)
        {
            dataPtr += dataSize;
        }
        while (dataPtr >= dataSize)
        {
            dataPtr -= dataSize;
        }
    }
}
