#include "check_sub_seq.h"
#include "read-data.h"
#include <iostream>
using namespace std;
bool check_subseq(const vector<int>& pattern, const sequence& _seq) {
    int seq_index = 0;
    for(int i=0; i< pattern.size(); i++) {
        while(true) {
            if(seq_index >= _seq.seq.size()) {
                return false;
            }
            if(pattern[i] == _seq.seq[seq_index++]) {
                break;
            }
        }
    }
    return true;
}

/**
 * For every pattern, check the subsequence
 */
void gen_feature_seq(const vector<vector<int> >&patterns,
        const vector<sequence>& seqs, vector<sequence>& result) {
    //For every sequence, check the features:
    vector<sequence>::const_iterator it_seq;
    vector<vector<int> >::const_iterator it_pat;
    int has_feature_count;
    for(it_seq = seqs.begin(); it_seq!=seqs.end();it_seq++) {
        sequence tmp;
        tmp.label = it_seq->label;
        bool has_feature = false;
        bool res;
        for(it_pat = patterns.begin(); it_pat!=patterns.end(); it_pat++) {
            if(res = check_subseq(*it_pat,*it_seq)){
                has_feature = true;
            }
            tmp.seq.push_back(res);
        }
        if(has_feature) {
            has_feature_count++;
        }
        result.push_back(tmp);
    }
}



