package net.ulfheim.mutato.battle;

import java.io.FileNotFoundException;
import java.io.IOException;
import net.ulfheim.mutato.Compiler;
import net.ulfheim.mutato.Muto;
import net.ulfheim.mutato.MutoMill;
import net.ulfheim.mutato.exception.CompilerException;
import net.ulfheim.mutato.impl.MutoImpl;
import net.ulfheim.mutato.impl.MutoMillImpl;
import org.apache.log4j.Logger;

/**
 * Muto Battle!
 * @author mdriscoll
 */
public class Battle 
{
    private static final Logger logger = Logger.getLogger(Battle.class.getName());

    public static void main( String[] args )
    {
        final String usage = "Usage: prog [source1] [source2]";

        if (args.length != 2)
        {
            logger.fatal(usage);
            System.exit(127);
        }

        try
        {
            MutoMill mill1 = new MutoMillImpl(256);
            MutoMill mill2 = new MutoMillImpl(256);

            String code1 = Compiler.readProgram(args[0]);
            String code2 = Compiler.readProgram(args[1]);

            int offset1 = 0;
            int offset2 = 128;

            Compiler.translateProgram(mill1, code1, offset1);
            Compiler.translateProgram(mill2, code2, offset2);

            Muto muto1 = new MutoImpl(mill1, mill2, offset1, code1.length());
            Muto muto2 = new MutoImpl(mill2, mill1, offset2, code2.length());

            int rounds = Integer.valueOf(System.getProperty("rounds", "1000000"));

            BattleResult result = fight(muto1, muto2, rounds);
            switch (result.winner)
            {
                case 1:
                    System.out.println("first program wins after " + result.rounds + " rounds");
                    System.exit(1);
                case 2:
                    System.out.println("second program wins after " + result.rounds + " rounds");
                    System.exit(2);
                default:
                case 0:
                    System.out.println("tie after " + result.rounds + " rounds");
                    System.exit(0);
            }
        }
        catch (CompilerException ex)
        {
            logger.fatal("program too large", ex);
            System.exit(127);
        }
        catch (FileNotFoundException ex)
        {
            logger.fatal("source file not found", ex);
            logger.fatal(usage);
            System.exit(127);
        }
        catch (IOException ex)
        {
            logger.fatal("unable to read source file", ex);
            System.exit(127);
        }
    }

    private static BattleResult fight(Muto muto1, Muto muto2, int rounds)
    {
        for (int i = 0; i < rounds; i++)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("starting round " + i);
                muto1.logState();
                muto2.logState();
            }

            boolean died;
            died = muto1.step();
            if (died)
                return new BattleResult(2, i);

            died = muto2.step();
            if (died)
                return new BattleResult(1, i);
        }

        return new BattleResult(0, rounds);
    }
}