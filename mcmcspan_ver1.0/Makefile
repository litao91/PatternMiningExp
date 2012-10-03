CXX = g++
CXXFLAGS = -O3 -W -Wall -ansi
LINKFLAGS =  -lm

mcmcspan: main.o utility.o mcmc_span.o
	$(CXX) $(CXXFLAGS) main.o utility.o mcmc_span.o $(LINKFLAGS) -o mcmc_span

main.o:main.cc mcmc_span.h
	$(CXX) $(CXXFLAGS) -c main.cc

uitlity.o:utility.cc utility.h
	$(CXX) $(CXXFLAGS) -c utility.cc

mcmc_span.o:mcmc_span.h mcmc_span.cc utility.h
	$(CXX) $(CXXFLAGS) -c mcmc_span.cc

clean:
	rm -f mcmcspan *.o *~
