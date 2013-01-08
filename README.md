mutato
======

A mashup of CoreWars, Brainfuck, and genetic algorithms.  The intent
is to invent a simple language and discover emergent complexity via
genetic algorithms.

quickstart
----------
To run a hundred generations of twenty battles each, execute the
following:

    ./genehill programs/ 20 100

This starts with the simple demo programs in `programs/` and evolves
from there.

In each generation, the top half scoring programs are preserved and
bred into a new generation.

The results of each generation are found in `results.{timestamp}/nn/`.
The resulting programs are named in the format
`{rank}-{original_program}-{generation}`.

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

Mills are a set of (typically) 2^n cells, each with a possible
value of 0-7.

code mill
---------

Each muto's code is placed in a random location in its code mill.
This code is executed in a loop.  The muto cannot modify its own
code or escape the loop.

data mill
---------

Each muto has access to a data mill which contains the enemy muto's
code somewhere within.  The muto can modify the data mill but has
no way to inspect it.

mutocode
--------

The following instructions are understood by the muto machine:

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

In C pseudo-code they are implemented in the following way:

*   `+` => `(*p)++`
*   `-` => `(*p)--`
*   `>` => `p++`
*   `<` => `p--`
*   `^` => `p += *p * (data.size / 16)`
*   `v` => `p -= *p * (data.size / 16)`

The numeric values of instructions can be modified in `MutoCode.java`.
Changing the numbers has a noticeable effect on the evolution of
the programs.

steps
-----

Each instruction runs in one step, and reaching the end of the code
loop also takes one step.  The two battling mutos execute steps in turn.
