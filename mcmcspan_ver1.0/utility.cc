#include "utility.h"
#include <iostream>
using namespace std;

/**
 * Check whether a pattern is a subsequence of another sequence.
 */
bool is_subseq(const vector<unsigned int>& _pattern,
        const vector<unsigned int>& _seq) {
    unsigned int seq_index = 0;
    for(unsigned int i = 0; i < _pattern.size(); i++) {
        while(true) {
            if(seq_index >= _seq.size()) {
                return false;
            }
            if(_pattern[i] == _seq[seq_index++]) {
                break;
            }
        }
    }
    return true;
}

int check_support(const vector<vector<unsigned int> >& _db,
        const vector<unsigned int>&  _pattern) {
    vector<vector<unsigned int> >::const_iterator db_it;
    int support_count = 0;
    for(db_it = _db.begin(); db_it!= _db.end(); db_it++) {
        if(is_subseq(_pattern, *db_it)) {
            support_count++;
        }
    }

    return support_count;
}

void print_seq(const vector<unsigned int>& _seq) {
    vector<unsigned int>::const_iterator it;
    for(it = _seq.begin(); it!=_seq.end(); it++) {
        cout<<*it<<' ';

    }
    cout <<endl;
}

