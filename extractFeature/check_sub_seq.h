#include <vector>
struct sequence;
using namespace std;
#ifndef CHECK_SUB_SEQ_H
#define CHECK_SUB_SEQ_H
bool check_subseq(const vector<int>& pattern, const sequence& seq);
void gen_feature_seq(const vector<vector<int> >&patterns,
        const vector<sequence>& seqs, vector<sequence>& result);

#endif
