#include "Prefixspan.h"

/********************************************************************
 * Read file
 ********************************************************************/
void Prefixspan::read(const string &_filename, Pairdata &pairdata) {
  string       line;
  int          item;
  unsigned int id = 0;

  ifstream is(_filename.c_str());
  //Transaction, pair with an integer and a vector with unsigned integers.
  Transaction transaction;
  while (getline (is, line)) {
    transaction.second.clear();
    vector<unsigned int> &itemsets = transaction.second;
    istrstream istrs ((char *)line.c_str());

    //push every thing in a sequence to the itemset vector.
    while (istrs >> item){
      itemsets.push_back(item);
    }

    transaction.first = id++;

    pairdata.database.push_back(transaction);
    pairdata.indices.push_back(0);
  }
}

/********************************************************************
 * Run prefixspan
 ********************************************************************/
void Prefixspan::run(const string &_filename)
{
    Pairdata pairdata;
    read(_filename, pairdata);
    project(pairdata);
}

/********************************************************************
 * Print frequent sequential patterns
 ********************************************************************/
void Prefixspan::print_pattern(Pairdata &projected) {
    //print out the pattern
    for (vector<unsigned int>::iterator it = pattern.begin(); it != pattern.end(); it++) {
        cout << *it << " ";
    }
    cout<<endl;

    //The ids that contains the pattern
    /*
    cout << endl << "( ";
    for (vector<Transaction>::iterator it = projected.database.begin(); it != projected.database.end(); it++) {
        cout << it->first << " ";
    }
    cout << ") : " << projected.database.size() << endl;
    */
}

/********************************************************************
 * Project database
 ********************************************************************/
/*
 * 1. Collect all the entries occur in the suffix.of all the sequences.
 * 2. Find the pattern with one more entry and do DFS recursively
 */
void Prefixspan::project(Pairdata &projected) {
    if (projected.database.size() < min_sup)
        return;
    print_pattern(projected);

    if (max_pat != 0 && pattern.size() == max_pat)
        return;

    /*
     * map_item:
     *   id, the entry of the sequence
     *   count
     */
    map<unsigned int, unsigned int> map_item;
    const vector<Transaction> &database = projected.database;

    //Basically count the number of occurrence of each entry in
    //all the sequence from the indices.
    for (unsigned int i = 0; i < database.size(); i++) {
        const vector<unsigned int> &itemset = database[i].second; //get the sequence
        //Iterate the sequence from the index, increase the indices by one.
        for (unsigned int iter = projected.indices[i]; iter < itemset.size(); iter++){
            ++map_item[itemset[iter]];
        }
    }

    Pairdata pairdata;
    vector<Transaction> &new_database = pairdata.database;
    vector<unsigned int> &new_indices = pairdata.indices;
    for (map<unsigned int, unsigned int>::iterator it_1 = map_item.begin(); it_1 != map_item.end(); it_1++) {
        //check for closed pattern
        if(it_1->second == pattern.size()) {  //if the enlarged prefix still equal to the original pattern
            cur_closed_pattern.push_back(it_1->first); //increase the closed pattern
        } else {
            closed_patterns.push_back(cur_closed_pattern);
            closed_patterns.clear();
        }
        for (unsigned int i = 0; i < database.size(); i++) {
            const Transaction &transaction = database[i];
            const vector<unsigned int> &itemset = transaction.second;
            for (unsigned int iter = projected.indices[i]; iter < itemset.size(); iter++) {
                if (itemset[iter] == it_1->first) {
                    //New database is the database with the same prefix
                    new_database.push_back(transaction);
                    new_indices.push_back(iter + 1);
                    break;
                }
            }
        }

        pattern.push_back(it_1->first);
        project(pairdata);
        pattern.pop_back();
        pairdata.clear();
    }
}

void Prefixspan::print_closed_pattern() const {
    cout<<"Closed pattern:"<<endl;
    vector<vector<unsigned int> >::const_iterator it;
    vector<unsigned int>::const_iterator it_2;
    for(it=closed_patterns.begin(); it!=closed_patterns.end(); it++) {
        for(it_2 = it->begin(); it_2!=it->end();it_2++) {
            cout<<*it_2<<" ";
        }
        cout<<endl;
    }
}

