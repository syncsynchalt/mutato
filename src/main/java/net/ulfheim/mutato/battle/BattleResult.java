package net.ulfheim.mutato.battle;

/**
 *
 * @author mdriscoll
 */
public class BattleResult
{
    public final int winner;
    public final long ticks;

    public BattleResult(int winner, long ticks)
    {
        this.winner = winner;
        this.ticks = ticks;
    }

}
