#include<iostream>
#include<vector>
#include<set>
#include<fstream>
#include<sstream>

#ifndef _MCMC_SPAN_H_
#define _MCMC_SPAN_H_

/**
 * Comparison function for the map data structure
 */
struct CompareSeq {
    bool operator()(const std::vector<unsigned int>& seq_1, const std::vector<unsigned int>& seq_2);
};

class McmcSpan {
  public:
    McmcSpan(unsigned int _min_sup):_m_min_sup(_min_sup){};
    void read_data(const std::string& filename);
    void run_alg();
    void print_max_pattern();
    void print_patterns();
  private:
    std::vector<std::vector<unsigned int> > _m_database;
    int _m_min_sup;
    std::set<std::vector<unsigned int>, CompareSeq> _m_patterns;
    std::set<std::vector<unsigned int>, CompareSeq> _m_max_patterns;
    //std::vector<std::vector<unsigned int> > _m_max_patterns;
};

#endif
