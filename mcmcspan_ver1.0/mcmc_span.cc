#include "mcmc_span.h"
#include "utility.h"
#include <ctime>
#include <iostream>
#include <cstdlib>
using namespace std;

//Return true if seq_1 < seq_2
bool CompareSeq::operator()(const vector<unsigned int>& seq_1, const vector<unsigned int>& seq_2) {
    vector<unsigned int>::const_iterator it_1, it_2;
    for(it_1 = seq_1.begin(),it_2 = seq_2.begin();; it_1++, it_2++) {
        if(it_1 == seq_1.end() && it_2 == seq_2.end()){
            return false;
        }else if(it_1==seq_1.end()) {
            return true;
        }else if(it_2 == seq_2.end()) {
            return false;
        }else if(*it_1 < *it_2) {
            return true;
        }else if(*it_1 > *it_2) {
            return false;
        }
    }
}

void McmcSpan::read_data(const string& _filename) {
    string line;
    unsigned int item;
    ifstream is(_filename.c_str());
    _m_database.clear();
    while(getline(is, line)) {
        stringstream istr((char *)line.c_str());
        _m_database.push_back(vector<unsigned int>());
        while(istr >> item) {
            _m_database.back().push_back(item);
        }
    }
}

void McmcSpan::run_alg() {
    vector<unsigned int> cur_pattern;
    vector<unsigned int> tmp_pattern;
    vector<vector<unsigned int> >surround_states;
    srand(time(NULL));
    while( _m_max_patterns.size()<= 20) {
        //Generate surrounding states
        //Hard code 25 states here
        surround_states.clear();
        tmp_pattern.clear();
        tmp_pattern = cur_pattern;
        for(int i=1; i<=25; i++) {
            //If the state is not discovered, we need to check it's support
            tmp_pattern.push_back(i);
            if(_m_patterns.find(tmp_pattern)==_m_patterns.end()) {
                int count = check_support(_m_database, tmp_pattern);
                if(count < _m_min_sup) {
                    tmp_pattern.pop_back();
                    continue;
                }
            }
            surround_states.push_back(tmp_pattern);
            tmp_pattern.pop_back();
        }

        if(surround_states.size()==0) {
            _m_max_patterns.insert(cur_pattern);
            cur_pattern.clear();
            continue;
        }

        int rand_base = surround_states.size()+1;
        int rand_num = rand() % rand_base;
        if(rand_num == 0) {
            cur_pattern.pop_back();
        } else {
            cur_pattern = surround_states[rand_num-1];
        }
        _m_patterns.insert(cur_pattern);
    }
}

void McmcSpan::print_max_pattern() {
    set<vector<unsigned int>,CompareSeq >::const_iterator it;
    vector<unsigned int>::const_iterator it_2;

    for(it=_m_max_patterns.begin(); it!=_m_max_patterns.end();it++) {
        for(it_2 = it->begin();it_2!=it->end();it_2++) {
            cout<<*it_2<<" ";
        }
        cout << endl;
    }
}

void McmcSpan::print_patterns() {
}
