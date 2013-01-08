/*
 * Copyright (c) 2013 PAX8
 */

package net.ulfheim.mutato.hill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.ulfheim.mutato.battle.Battle;
import net.ulfheim.mutato.battle.BattleResult;
import net.ulfheim.mutato.exception.CompilerException;

/**
 *
 * @author mdriscoll
 */
public class Hill
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Usage: hill [dir] [rounds]");
            System.exit(1);
        }


        String dir = args[0];
        int rounds = Integer.valueOf(args[1]);

        File dirFile = new File(dir);
        String[] files = dirFile.list();
        List<HillFighter> progs = new ArrayList<HillFighter>();

        try
        {
            for (String file : files)
            {
                String code = net.ulfheim.mutato.Compiler.readProgram(dir + "/" + file);
                progs.add(new HillFighter(code, file));
            }

            Random random = new Random();
            for (int i = 0; i < rounds; i++)
            {
                int n1 = random.nextInt(progs.size());
                HillFighter p1 = progs.get(n1);
                int n2;
                do
                {
                    n2 = random.nextInt(progs.size());
                } while (n1 == n2);
                HillFighter p2 = progs.get(n2);

                BattleResult result = Battle.fight(p1.getCode(), p2.getCode(), 1000000);
                switch (result.winner)
                {
                    case 0:
                        System.out.println("tied");
                        p1.markTie();
                        p2.markTie();
                        break;
                    case 1:
                        System.out.println(
                                p1.getFilename() + " beats "
                                + p2.getFilename() + " after "
                                + result.rounds + " rounds");
                        p1.markWin();
                        p2.markLoss();
                        break;
                    case 2:
                        System.out.println(
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

            System.out.println("\nResults:");
            for (HillFighter fighter : progs)
            {
                System.out.println(fighter.getFilename() + ": " + fighter.getScore());
            }
        }
        catch (CompilerException ex)
        {
            System.out.println(ex);
            System.exit(1);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex);
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.out.println(ex);
            System.exit(1);
        }
    }
}
