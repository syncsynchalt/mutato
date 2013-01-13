package net.ulfheim.mutato.hill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.ulfheim.mutato.battle.Battle;
import net.ulfheim.mutato.battle.BattleResult;
import net.ulfheim.mutato.exception.CompilerException;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 * @author mdriscoll
 */
public class Hill
{
    public static List<HillFighter> rankHill(
            List<HillFighter> progs, int fightsPerProgram, long ticks)
            throws CompilerException
    {
        if (progs.size() < 2)
        {
            throw new IllegalArgumentException(
                    "Not enough programs, " + progs.size() + " given but 2 required");
        }

        Random random = new Random();
        for (int i = 0; i < progs.size(); i++)
        {
            HillFighter p1 = progs.get(i);
            for (int j = i+1; j < progs.size(); j++)
            {
                HillFighter p2 = progs.get(j);

                for (int fight = 0; fight < fightsPerProgram; fight++)
                {
                    BattleResult result = Battle.fight(p1.getCode(), p2.getCode(), ticks);
                    switch (result.winner)
                    {
                        case 0:
                            logger.debug("tied");
                            p1.markTie();
                            p2.markTie();
                            break;
                        case 1:
                            logger.debug(
                                    p1.getFilename() + " beats "
                                    + p2.getFilename() + " after "
                                    + result.ticks + " ticks");
                            p1.markWin();
                            p2.markLoss();
                            break;
                        case 2:
                            logger.debug(
                                    p2.getFilename() + " beats "
                                    + p1.getFilename() + " after "
                                    + result.ticks + " ticks");
                            p1.markLoss();
                            p2.markWin();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        Collections.sort(progs, new HillFighterScoreSort());
        return progs;
    }

    private static class HillFighterScoreSort implements Comparator<HillFighter>
    {
        @Override
        public int compare(HillFighter o1, HillFighter o2)
        {
            return o2.getScore() - o1.getScore();
        }
    }



    private static final Logger logger = Logger.getLogger(Hill.class.getName());

    @Option(name="-ticks", usage="number of fight ticks before tie declared (default:10000000)")
    private Long ticks = 10000000L;

    @Option(name="-millsize", usage="number of cells in code/data mills (default:65536)")
    private Integer millSize = 65536;

    @Option(name="-loopticks", usage="number of ticks to restart loop (default:2)")
    private Integer loopTicks = 2;

    @Option(name="-dir", usage="directory of programs to use on hill (default:./programs)")
    private String programDir = "./programs";

    @Option(name="-fights", usage="number of fights per program cross (default:1)")
    private Integer fightsPerCross = 1;

    public static void main(String[] args)
    {
        new Hill().doMain(args);
    }

    private void doMain(String[] args)
    {
        CmdLineParser parser = new CmdLineParser(this);
        try
        {
            parser.parseArgument(args);

        } catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("Usage: hill [args]");
            parser.printUsage(System.err);
            System.err.println();
            System.exit(127);
        }
        System.setProperty("muto.loopticks", loopTicks.toString());
        System.setProperty("muto.millsize", millSize.toString());

        File dirFile = new File(programDir);
        String[] files = dirFile.list();
        List<HillFighter> progs = new ArrayList<HillFighter>();

        try
        {
            for (String file : files)
            {
                String code = net.ulfheim.mutato.Compiler.readProgram(programDir + "/" + file);
                progs.add(new HillFighter(code, file, 0));
            }

            progs = rankHill(progs, fightsPerCross, ticks);

            for (HillFighter fighter : progs)
            {
                System.out.println(" result: " + fighter.getFilename() + " scored " + fighter.getScore());
            }
        }
        catch (CompilerException ex)
        {
            System.err.println(ex);
            System.exit(1);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println(ex);
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.err.println(ex);
            System.exit(1);
        }
    }

}
