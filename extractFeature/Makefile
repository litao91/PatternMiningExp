CXX = g++

CXXFLAGS = -g
LINKFLAGS = -lm

main: main.o read-data.o check_sub_seq.o
	$(CXX) $(LINKFLAGS) $(CXXFLAGS)  -o main read-data.o main.o check_sub_seq.o

main.o:main.cc read-data.h
	$(CXX) $(CXXFLAGS) -c main.cc

read-data.o:read-data.cc read-data.h
	$(CXX) $(CXXFLAGS) -c read-data.cc

check_sub_seq.o:check_sub_seq.cc
	$(CXX) $(CXXFLAGS) -c check_sub_seq.cc

clean:
	rm -rf main *.o *~

