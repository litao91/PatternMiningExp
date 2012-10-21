#!/usr/bin/python2
import random
import sys

def getlines(filename):
    with open(filename) as f:
        return list(f.readlines())

def poll():
    return random.randint(0, 1000) < 203

def main():
    filename = sys.argv[1]
    newfiles = sys.argv[2]
    print "file to separate is %s" % filename
    print "newfile is %s" % newfiles

    fp20 = open(newfiles + 'p20', 'w')
    fp80 = open(newfiles + 'p80', 'w')
    np20 = np80 = 0

    for line in getlines(filename):
        if poll():
            fp20.write(line)
            np20 += 1
        else:
            fp80.write(line)
            np80 += 1

    print 'file is separated to %s / %s'% (np20, np80)

if __name__ == '__main__':
    main()



