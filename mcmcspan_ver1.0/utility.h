#include<vector>
using std::vector;

#ifndef _UTILITY_H
#define _UTILITY_H
bool is_subseq(const vector<unsigned int>& pattern,
        const vector<unsigned int>& _seq);
int check_support(const vector<vector<unsigned int> >& _db,
        const vector<unsigned int>& _pattern);
void print_seq(const vector<unsigned int>& _seq);

#endif
