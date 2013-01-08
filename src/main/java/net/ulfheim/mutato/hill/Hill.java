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

/**
 *
 * @author mdriscoll
 */
public class Hill
{
    private static final Logger logger = Logger.getLogger(Hill.class.getName());

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Usage: hill [program dir] [fights per program]");
            System.exit(1);
        }

        String dir = args[0];
        int fightsPerProgram = Integer.valueOf(args[1]);

        File dirFile = new File(dir);
        String[] files = dirFile.list();
        List<HillFighter> progs = new ArrayList<HillFighter>();

        try
        {
            for (String file : files)
            {
                String code = net.ulfheim.mutato.Compiler.readProgram(dir + "/" + file);
                progs.add(new HillFighter(code, file, 0));
            }

            progs = fight(progs, fightsPerProgram);

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

    public static List<HillFighter> fight(List<HillFighter> progs, int fightsPerProgram)
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

                BattleResult result = Battle.fight(p1.getCode(), p2.getCode(), 1000000);
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
                                + result.rounds + " rounds");
                        p1.markWin();
                        p2.markLoss();
                        break;
                    case 2:
                        logger.debug(
                                p2.getFilename() + " beats "
                                + p1.getFilename() + " after "
                                + result.rounds + " rounds");
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
}
