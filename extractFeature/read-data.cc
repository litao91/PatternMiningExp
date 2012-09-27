//A quick dirty implementation of I/O
#include <fstream>
#include <sstream>
#include <iostream>
#include "read-data.h"
using namespace std;
//file scope variable for temporary usage.

void get_freq_seq(char const* filename, vector<vector<int> >& freq_seq) {
    string line;
    ifstream is(filename);
    int item;
    while(getline(is,line)) {
        stringstream istr((char *)line.c_str());
        freq_seq.push_back(vector<int>());
        while(istr>>item) {
            freq_seq.back().push_back(item);
        }
    }
}

void get_seqs(char const* filename, vector<sequence>& seqs) {
    string line;
    ifstream is(filename);
    int item;
    while(getline(is,line)) {
        stringstream istr((char *)line.c_str());
        istr >> item;
        sequence one_seq;
        one_seq.label = item;
        while(istr>>item) {
            one_seq.seq.push_back(item);
        }
        seqs.push_back(one_seq);
    }
}

void print_seq(const vector<sequence>& seqs) {
    vector<sequence>::const_iterator it;
    for(it = seqs.begin(); it!=seqs.end();it++) {
        cout<<it->label<<" ";
        for(int i=0; i< it->seq.size();i++) {
            cout<<i<<":"<<it->seq[i]<<" ";
        }
        cout << endl;
    }
}











