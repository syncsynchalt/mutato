/*
 * Copyright (c) 2012 PAX8
 */

package net.ulfheim.mutato;

/**
 *
 * @author mdriscoll
 */
public interface MutoMill
{

    void decr(int index);

    int get(int index);

    void incr(int index);

    int size();

}
