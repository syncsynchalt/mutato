/*
 * Copyright (c) 2013 PAX8
 */

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
    public static List<HillFighter> fight(List<HillFighter> progs, int fightsPerProgram, long ticks, int millSize)
            throws CompilerException
    {
        if (progs.size() < 2)
        {
            throw new IllegalArgumentException(
                    "Not enough programs, " + progs.size() + " given but 2 required");
        }

        Random random = new Random();
        for (HillFighter p1 : progs)
        {
            for (int i = 0; i < fightsPerProgram; i++)
            {
                HillFighter p2;
                do
                {
                    int n2 = random.nextInt(progs.size());
                    p2 = progs.get(n2);
                } while (p1 == p2);

                BattleResult result = Battle.fight(p1.getCode(), p2.getCode(), ticks, millSize);
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

    @Option(name="-dir", usage="directory of programs to use on hill (default:./programs)")
    private String programDir = "./programs";

    @Option(name="-fights", usage="number of fights per program ***")
    private Integer fightsPerProgram;

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

            if (fightsPerProgram == null)
                throw new CmdLineException(parser, "Argument -fights is required");

        } catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("Usage: hill [args]");
            parser.printUsage(System.err);
            System.err.println();
            System.exit(127);
        }

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

            progs = fight(progs, fightsPerProgram, ticks, millSize);

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
