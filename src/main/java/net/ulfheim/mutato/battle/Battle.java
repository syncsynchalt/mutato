package net.ulfheim.mutato.battle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.ulfheim.mutato.Compiler;
import net.ulfheim.mutato.Muto;
import net.ulfheim.mutato.MutoMill;
import net.ulfheim.mutato.exception.CompilerException;
import net.ulfheim.mutato.impl.MutoImpl;
import net.ulfheim.mutato.impl.MutoMillImpl;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Muto Battle!
 * @author mdriscoll
 */
public class Battle 
{
    public static BattleResult fight(String code1, String code2, long ticks, int millSize)
            throws CompilerException
    {
        MutoMill mill1 = new MutoMillImpl(millSize);
        MutoMill mill2 = new MutoMillImpl(millSize);

        Random rand = new Random();
        int offset1 = rand.nextInt(mill1.size());
        int offset2 = rand.nextInt(mill2.size());

        Compiler.translateProgram(mill1, code1, offset1);
        Compiler.translateProgram(mill2, code2, offset2);

        Muto muto1 = new MutoImpl(mill1, mill2, offset1, code1.length());
        Muto muto2 = new MutoImpl(mill2, mill1, offset2, code2.length());

        return fight(muto1, muto2, ticks);
    }

    private static BattleResult fight(Muto muto1, Muto muto2, long ticks)
    {
        for (long i = 0; i < ticks; i++)
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

        return new BattleResult(0, ticks);
    }



    private static final Logger logger = Logger.getLogger(Battle.class.getName());

    @Option(name="-ticks", usage="number of fight ticks before tie declared (default:10000000)")
    private Long ticks = 10000000L;

    @Option(name="-millsize", usage="number of cells in code/data mills (default:65536)")
    private Integer millSize = 65536;

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args)
    {
        new Battle().doMain(args);
    }

    private void doMain(String[] args)
    {
        CmdLineParser parser = new CmdLineParser(this);
        try
        {
            parser.parseArgument(args);
            if (arguments.size() != 2)
                throw new CmdLineException(parser, "Two fighters needed, gave " + arguments.size());

        } catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("Usage: battle [args] [source1] [source2]");
            parser.printUsage(System.err);
            System.err.println();
            System.exit(127);
        }

        try
        {
            String prog1 = arguments.get(0);
            String prog2 = arguments.get(1);
            String code1 = Compiler.readProgram(prog1);
            String code2 = Compiler.readProgram(prog2);

            BattleResult result = fight(code1, code2, ticks, millSize);
            switch (result.winner)
            {
                case 1:
                    System.out.println(prog1 + " wins after " + result.ticks + " ticks");
                    System.exit(1);
                case 2:
                    System.out.println(prog2 + " wins after " + result.ticks + " ticks");
                    System.exit(2);
                default:
                case 0:
                    System.out.println("tie after " + result.ticks + " ticks");
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
            System.exit(127);
        }
        catch (IOException ex)
        {
            logger.fatal("unable to read source file", ex);
            System.exit(127);
        }
    }

}