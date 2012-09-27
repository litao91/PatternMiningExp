#ifndef _PREFIXSPAN_H_
#define _PREFIXSPAN_H_

#include <iostream>
#include <vector>
#include <string>
#include <map>
#include <fstream>
#include <strstream>

using namespace std;

/*
 * Transaciton:
 * first: id of the sequence
 * second: itemsets, basically a sequence
 */

typedef pair<unsigned int, vector<unsigned int> > Transaction; //<id <itemsets> >

/**
 * database: vector of sequences.
 * indices: the projected index for each item in database.
 */
struct Pairdata {
  vector<Transaction> database;
  vector<unsigned int> indices;
  void clear() {
      database.clear();
      indices.clear();
  }
};

class Prefixspan {
  unsigned int min_sup;
  unsigned int max_pat;
  vector<unsigned int> pattern;
  vector<unsigned int> cur_closed_pattern;
  vector<vector<unsigned int> > closed_patterns;

public:
  Prefixspan(unsigned int _min_sup, unsigned int _max_pat) : min_sup(_min_sup), max_pat(_max_pat){};
  void read(const string &_filename, Pairdata &pairdata);
  void print_pattern(Pairdata &projected);
  void print_closed_pattern() const;
  void run(const string &_filename);
  void project(Pairdata &projected);
};

#endif // _PREFIXSPAN_H
