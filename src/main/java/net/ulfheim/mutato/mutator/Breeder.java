package net.ulfheim.mutato.mutator;

import java.util.Random;
import net.ulfheim.mutato.MutoCode;

/**
 *
 * @author mdriscoll
 */
public class Breeder
{
    public static final Random random = new Random();

    public static String mutate(String code, String optionalCross)
    {
        int type = random.nextInt(8);
        switch (type)
        {
            case 0:
            case 1:
                return pointChange(code);
            case 2:
            case 3:
                return pointRemoval(code);
            case 4:
            case 5:
                return pointAdd(code);
            case 6:
            case 7:
                return crossCode(code, optionalCross);
            default:
                throw new IllegalStateException("unexpected random result");
        }
    }

    private static String pointChange(String code)
    {
        if (code.isEmpty())
            return "";

        int where = random.nextInt(code.length());
        int orig = MutoCode.valueOf(code.charAt(where));
        int newCode;
        do {
            newCode = random.nextInt(MutoCode.MAX);
        } while (newCode == MutoCode.DIE || newCode == orig);

        return code.substring(0, where) + MutoCode.codeChar(newCode) + code.substring(where+1);
    }

    private static String pointRemoval(String code)
    {
        if (code.isEmpty())
            return "";

        int where = random.nextInt(code.length());
        return code.substring(0, where) + code.substring(where+1);
    }

    private static String pointAdd(String code)
    {
        int where = code.length() == 0 ? 0 : random.nextInt(code.length()+1);
        int newCode;
        do {
            newCode = random.nextInt(MutoCode.MAX);
        } while (newCode == MutoCode.DIE);

        return code.substring(0, where) + MutoCode.codeChar(newCode) + code.substring(where);
    }

    private static String crossCode(String code, String cross)
    {
        return code + cross;
    }
}
