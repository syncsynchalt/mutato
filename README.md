mutato
======

A mashup of CoreWars, Brainfuck, and genetic algorithms.  The intent
is to invent a simple language and discover emergent complexity via
genetic algorithms.

mutos
-----

The programs are called mutos, and they execute mutocode in a pair
of mills.  Each muto has two mills, a code mill and a data mill.
Each muto is battling with an opposite muto with interlinked mills,
so that one muto's code mill is the other's data mill.

Each muto is attempting to get the other muto to execute a DIE
instruction.

mills
-----

Mills are a set of 2^n addressable cells, each with a possible value
of 0-7.

code mill
---------

The muto's code is placed in a random location in the code mill.
This code is executed in a loop.  The muto cannot modify their own
code or escape the loop.

data mill
---------

The muto has access to a data mill which contains the enemy muto's
code somewhere within.  The muto can modify the data mill but has
no way to inspect it.

mutocode
--------

The following opcodes are understood by the muto machine:

*   `. NOOP`
    Perform no action.

*   `+ INCR`
    Increment the current item in the muto's data mill.

*   `- DECR`
    Decrement the current item in the muto's data mill.

*   `> NEXT`
    Increment the data pointer in the muto's data mill.

*   `< PREV`
    Decrement the data pointer in the muto's data mill.

*   `^ FFWD`
    Increment the data pointer by current item times sixteenth of data mill size.

*   `v RWND`
    Decrement the data pointer by current item times sixteenth of data mill size.

*   `x DIE`
    Forfeit the battle.

The numeric values of opcodes can be modified in `MutoCode.java`.
Changing the numbers has a noticeable effect on the evolution of
the programs.
