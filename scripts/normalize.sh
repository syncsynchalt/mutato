#!/bin/sh -e
#
# Copy genehill results into a new directory, with a normalized label
#
# In other words this:
#
#   $ ls result.nnn/50/
#   001-imp-13 002-imp-9 003-hopper-17
#
#   $ scripts/normalize.sh result.nnn/50/ newprograms/ eden1
#   $ ls newprograms/
#   eden1-001 eden1-002 eden1-003
#
# Usage: $0 [inputdir] [outputdir] [label]

inputdir=$1
outputdir=$2
label=$3

mkdir -p $outputdir

for i in $(cd $inputdir && ls *); do
    newname="$label-$(echo $i | sed -e s/-.*//)"
    cp "$inputdir/$i" "$outputdir/$newname"
done
