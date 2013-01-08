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
import net.ulfheim.mutato.exception.CompilerException;
import net.ulfheim.mutato.mutator.Breeder;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
public class GeneticHill
{
    private static final Logger logger = Logger.getLogger(GeneticHill.class.getName());

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.err.println(
                    "Usage: genetichill [start dir] [fights per program] [genetic rounds]");
            System.exit(1);
        }

        String startDir = args[0];
        int fightsPerProgram = Integer.valueOf(args[1]);
        int geneRounds = Integer.valueOf(args[2]);
        String resultsDir = "results." + System.currentTimeMillis()/1000;

        File dirFile = new File(startDir);
        String[] files = dirFile.list();
        List<HillFighter> progs = new ArrayList<HillFighter>();

        try
        {
            // pad out to at least 50 programs
            while (progs.size() < 50)
            {
                for (String file : files)
                {
                    String code = net.ulfheim.mutato.Compiler.readProgram(startDir + "/" + file);
                    progs.add(new HillFighter(code, file, 0));
                }
            }

            for (int i = 0; i < geneRounds + 1; i++)
            {
                progs = Hill.fight(progs, fightsPerProgram);

                for (HillFighter fighter : progs)
                {
                    logger.debug(" result: " + fighter.getFilename() + " scored " + fighter.getScore());
                }

                progs = breedNewGen(progs, resultsDir, i);
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

    private static List<HillFighter> breedNewGen(
            List<HillFighter> orig, String resultsDir, int geneRound) throws IOException
    {
        // write the old gen to disk
        File rdir = new File(resultsDir);
        rdir.mkdir();
        File subDir = new File(resultsDir + "/" + geneRound);
        subDir.mkdir();
        for (int rank = 0; rank < orig.size(); rank++)
        {
            HillFighter f = orig.get(rank);

            File out = new File(subDir, String.format("%03d-%s-%d",
                    rank+1, f.getFilename(), f.getGeneration()));
            FileUtils.write(out, f.getCode() + "\n", "UTF-8");
        }
        System.out.println("Wrote programs to " + subDir.getPath());

        // save top half of new gen
        List<HillFighter> newList = new ArrayList<HillFighter>();
        for (int rank = 0; rank < orig.size() / 2; rank++)
        {
            HillFighter f = orig.get(rank);
            newList.add(new HillFighter(f.getCode(), f.getFilename(), f.getGeneration()));
        }

        // make second half of new gen via mutation
        Random random = new Random();
        for (int rank = 0; rank < orig.size() / 2; rank++)
        {
            HillFighter f = orig.get(rank);
            HillFighter fCross = orig.get(random.nextInt(orig.size()));
            String newCode = Breeder.mutate(f.getCode(), fCross.getCode());

            newList.add(new HillFighter(newCode, f.getFilename(), f.getGeneration()+1));
        }

        return newList;
    }
}
