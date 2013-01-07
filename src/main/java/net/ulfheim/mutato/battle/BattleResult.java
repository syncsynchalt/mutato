/*
 * Copyright (c) 2012 PAX8
 */

package net.ulfheim.mutato.battle;

/**
 *
 * @author mdriscoll
 */
class BattleResult
{
    public final int winner;
    public final int rounds;

    public BattleResult(int winner, int rounds)
    {
        this.winner = winner;
        this.rounds = rounds;
    }

}
