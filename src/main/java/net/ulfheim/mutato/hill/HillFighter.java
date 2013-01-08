/*
 * Copyright (c) 2013 PAX8
 */

package net.ulfheim.mutato.hill;

/**
 *
 * @author mdriscoll
 */
public class HillFighter
{
    private int score;
    private String code;
    private String file;

    public HillFighter(String code, String file)
    {
        this.code = code;
        this.file = file;
    }

    public void markWin() { score += 3; }
    public void markLoss() {}
    public void markTie() { score += 1; }

    public int getScore() { return score; }
    public String getCode() { return code; }
    public String getFilename() { return file; }
}
