/*
 * Copyright (c) 2012 PAX8
 */

package net.ulfheim.mutato;

/**
 *
 * @author mdriscoll
 */
public final class MutoCode
{
    public static final int NOP  = 0;
    public static final int INCR = 1;
    public static final int DECR = 2;
    public static final int NEXT = 3;
    public static final int PREV = 4;
    public static final int FFWD = 5;
    public static final int RWND = 6;
    public static final int DIE  = 7;

    public static int valueOf(char c)
    {
        switch (c)
        {
            case '.': return NOP;
            case '+': return INCR;
            case '-': return DECR;
            case '>': return NEXT;
            case '<': return PREV;
            case '^': return FFWD;
            case 'V':
            case 'v': return RWND;
            case 'X':
            case 'x': return DIE;
            default:
                throw new IllegalArgumentException("Unrecognized mutocode " + c);
        }
    }
}
