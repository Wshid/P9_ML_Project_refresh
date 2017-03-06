#!/bin/sh
while read line
do
    echo $line
done

for i in $(ls); do
    echo FILE : $i
done