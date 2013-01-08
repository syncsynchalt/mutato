package net.ulfheim.mutato.battle;

/**
 *
 * @author mdriscoll
 */
public class BattleResult
{
    public final int winner;
    public final int rounds;

    public BattleResult(int winner, int rounds)
    {
        this.winner = winner;
        this.rounds = rounds;
    }

}
