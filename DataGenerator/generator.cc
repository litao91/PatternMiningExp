#include <cstdio>
#include <cstdlib>
#include <ctime>

int main(int argc, char** argv) {
    int min_seq_length = 0;
    int max_seq_length = 0;
    int item_num = 0;
    int seq_num = 0;

    min_seq_length = atoi(argv[1]);
    max_seq_length = atoi(argv[2]);
    item_num = atoi(argv[3]);
    seq_num = atoi(argv[4]);
    srand(time(NULL));


    printf("MinSeqLength:%d, maxSeqLenght:%d, item_num:%d, seqNum:%d\n",
            min_seq_length, max_seq_length, item_num, seq_num);

    for(int i=0; i< seq_num; i++) {
        int length = min_seq_length +
            rand() % (max_seq_length -min_seq_length);
        for(int j = 0; j < length; j++) {
            printf("%d ", rand()%item_num +1);
        }
        printf("\n");

    }
}
