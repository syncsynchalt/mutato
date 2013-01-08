package net.ulfheim.mutato;

/**
 *
 * @author mdriscoll
 */
public final class MutoCode
{
    public static final int NOOP = 0;
    public static final int FFWD = 1;
    public static final int INCR = 2;
    public static final int NEXT = 3;
    public static final int DIE  = 4;
    public static final int PREV = 5;
    public static final int DECR = 6;
    public static final int RWND = 7;

    public static final int MAX  = 8;

    public static int valueOf(char c)
    {
        switch (c)
        {
            case '.': return NOOP;
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

    public static char codeChar(int code)
    {
        switch (code)
        {
            case NOOP: return '.';
            case INCR: return '+';
            case DECR: return '-';
            case NEXT: return '>';
            case PREV: return '<';
            case FFWD: return '^';
            case RWND: return 'v';
            case DIE:  return 'x';
            default:
                throw new IllegalArgumentException("Unrecognized mutocodepoint " + code);
        }
    }
}
