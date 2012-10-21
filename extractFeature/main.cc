#include "read-data.h"
#include "check_sub_seq.h"

#include <cstdlib>
#include <iostream>
#include <vector>
using namespace std;
int main(int argc, char** argv) {
    if(argc <= 2) {
        cerr<< "Please specify filename\n";
        exit(0);
    }
    vector<vector<int> > freq_patterns;
    vector<sequence> seqs;

    vector<sequence> feature_seqs;
    get_freq_seq(argv[1], freq_patterns);
    get_seqs(argv[2], seqs);
    gen_feature_seq(freq_patterns, seqs, feature_seqs);
    print_seq(feature_seqs);
}


