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
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 * @author mdriscoll
 */
public class GeneticHill
{
    private static final Logger logger = Logger.getLogger(GeneticHill.class.getName());

    @Option(name="-ticks", usage="number of fight ticks before tie declared (default:10000000)")
    private Long ticks = 10000000L;

    @Option(name="-millsize", usage="number of cells in code/data mills (default:65536)")
    private Integer millSize = 65536;

    @Option(name="-loopticks", usage="number of ticks to restart loop (default:2)")
    private Integer loopTicks = 2;

    @Option(name="-dir", usage="directory of programs to start the breeds (default:./programs)")
    private String programDir = "./programs";

    @Option(name="-fights", usage="number of fights per program cross (default:1)")
    private Integer fightsPerCross = 1;

    @Option(name="-generations", usage="number of generations of hills to breed ***")
    private Integer generations;

    @Option(name="-minprograms", usage="minimum number of programs in hill (default:50)")
    private Integer minimumProgs = 50;

    public static void main(String[] args)
    {
        new GeneticHill().doMain(args);
    }

    private void doMain(String[] args)
    {
        CmdLineParser parser = new CmdLineParser(this);
        try
        {
            parser.parseArgument(args);

            if (generations == null)
                throw new CmdLineException(parser, "Argument -generations is required");

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

        String resultsDir = "results." + System.currentTimeMillis()/1000;

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

            // pad out to at least minimumProgs programs
            int startSize = progs.size();
            while (progs.size() < minimumProgs)
            {
                for (String file : files)
                {
                    String code = net.ulfheim.mutato.Compiler.readProgram(programDir + "/" + file);
                    progs.add(new HillFighter(code, file, 0));
                    if (progs.size() >= minimumProgs)
                        break;
                }
            }
            if (progs.size() != startSize)
                logger.info("Copied program list to increase from " + startSize + " to " + progs.size());

            for (int i = 0; i < generations + 1; i++)
            {
                logger.info("starting generation " + i);
                long startTime = System.currentTimeMillis();

                progs = Hill.rankHill(progs, fightsPerCross, ticks);

                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime)/1000;
                logger.info("finished generation " + i + " in " + duration + " seconds, top score is " + progs.get(0).getScore());

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
