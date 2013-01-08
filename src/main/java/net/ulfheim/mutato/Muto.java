package net.ulfheim.mutato;

/**
 *
 * @author mdriscoll
 */
public interface Muto
{

    /**
     * Execute one instruction in our code mill.
     * @return whether we executed the death instruction
     */
    boolean step();

    /**
     * Print some debugging information to log4j.debug.
     */
    void logState();

}
