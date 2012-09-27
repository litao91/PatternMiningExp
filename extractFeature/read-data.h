#include <vector>
using std::vector;
#ifndef READ_FREQSEQ_H
#define READ_FREQSEQ_H
struct sequence{
    int label;
    vector<int> seq;
};
void get_freq_seq(char const* filename, vector<vector<int> >& freq_seq);
void get_seqs(char const* filename, vector<sequence>& seqs);
void print_seq(const vector<sequence>& seqs);
#endif
