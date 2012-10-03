#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <vector>
#include "mcmc_span.h"
using std::vector;
using std::string;

int min_sup = 1;
vector<string> filenames;

void parse_parameters(int, char**);


int main(int argc, char** argv) {
    parse_parameters(argc, argv);
    McmcSpan mcmc_span(min_sup);
    mcmc_span.read_data(filenames[0]);
    mcmc_span.run_alg();
    mcmc_span.print_max_pattern();
}

void parse_parameters(int argc, char** argv) {
    if(argc == 1) {
        printf("More arguments needed\n");
    }


    for(int argnum = 1; argnum < argc; argnum++) {
        if(argv[argnum][0] == '-') {
            if(!strcmp(argv[argnum], "-min_sup")) {
                if(argnum == argc - 1 ) {
                    fprintf(stderr, "Most specify minimum support after -min_sup\n");
                }
                min_sup = atoi(argv[++argnum]);
            }
        }else {
            filenames.push_back(argv[argnum]);
        }
    }
}
