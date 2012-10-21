#!/bin/sh
bin=/csproject/Wilfred/datamine/Trajectory/bin
dir=/csproject/Wilfred/datamine/Trajectory/EvenBiggerData

l_n=25
d_n=30
sup_n=100
k_n=20

if [ ! -d "./n" ]; then 
    mkdir n
fi

for((n = 100000; n<=500000; n=n+100000)) do
    cd $dir
    if [ ! -d "n/$n" ]; then
        mkdir $dir/n/$n
    fi
    cd $dir/n/$n
#generate data
    $bin/datagen $l_n $d_n $n &> gendata
#run mcmc
    (time java -jar $bin/Mcmc.jar -f ./gendata -min_sup `expr $n / $sup_n` -thread_num 1 -item_num $d_n -k $k_n ) &>  result.mcmc
#run prefix
    (time $bin/prefixspan -min_sup `expr $n / $sup_n` ./gendata) &> result.prefix
done


