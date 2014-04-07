#!/usr/bin/env python

import argparse
import os

__author__ = 'maplesod'


parser = argparse.ArgumentParser(prog="chunk_executor", description="Executes a bunch of trinity commands in sequence")
parser.add_argument('-i', '--input', metavar='PATH', required='true', type=str, help="The trinity file containing sequences to execute linearly")

args=parser.parse_args()

with open(str(args.input)) as f:
    commands_executed = 0
    for line in f:
        newline = line.replace('\"', '\'').rstrip('\n')
        print "Command: " + newline
        os.system(newline)
        commands_executed += 1

print "Number of commands executed: " + str(commands_executed)
