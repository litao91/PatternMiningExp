#!/bin/sh
# The procedure to do real experiment:
# 1. Run prefixspan algorithm, generate the frequent pattern
# 2. The frequency will be printed in the first column for each pattern.
# 3. Use the Sampler to find the max pattern and sample among them, named: pattern.prefix.max
# 4. Use Topk finder to find the top frequency, pattern.prefix.topk
# 5. Run mcmc, named pattern.mcmc
# 6. Run extracted feature, get feature.prefix.max feature.prefix.topk, feature.mcmc
# 7. Run sep.py to separate train and test
# 8. Run svm-train get model.prefix.max model.prefix.prefix.topk model.mcmc
# 9. Run svm-predict to get result.prefix.max result.prefix.topk result.mcmc

bin=/home/gary_li/Development/PatternMiningExp/Data_cleaned/bin
d1=/home/gary_li/Development/PatternMiningExp/Data_cleaned/d1
d2=/home/gary_li/Development/PatternMiningExp/Data_cleaned/d2
d3=/home/gary_li/Development/PatternMiningExp/Data_cleaned/d3

for((data_num = 2; data_num <= 2; data_num++)) do
    # Each data set corresponding to its own directory
    if [ $data_num == 1 ]; then
        n=1236
        d=25
        cd $d1
    elif [ $data_num == 2 ]; then
        cd $d2
        n=2048
        d=25
    elif [ $data_num == 3 ]; then
        cd $d3
        n=422
        d=100
    fi
    #setting up the current directory (experiment)
    cur_dir=`pwd`

    # Base
    sup=30
    k=20

    # for k 
    # prepare directory
    mkdir $cur_dir/k
    for((k = 8; k <= 30; k=k+2)) do
        # run mcmc

        # for specific k
        mkdir $cur_dir/k/$k
        cd $cur_dir/k/$k

        # run mcmc algorithm 
        java -jar $bin/mcmc.jar \
            -f $cur_dir/seq.dat \
            -k $k \
            -min_sup $sup\
            -thread_num 2\
            -item_num $d  > ./mcmc.freq

            #-min_sup $(($n/$sup))\
        #run prefixspan
        # $bin/prefixspan -min_sup $(($n/$sup)) $cur_dir/seq.dat > prefix.ori
        $bin/prefixspan -min_sup $sup $cur_dir/seq.dat > prefix.ori
        
        #sampling on prefix span
        java -jar $bin/TopkFinder.jar -k $k -f ./prefix.ori > prefix.topk
        java -jar $bin/maxpatsampler.jar -k $k -f ./prefix.ori > prefix.max

        $bin/extractFeature ./mcmc.freq $cur_dir/seq_with_label.dat > mcmc.feature
        $bin/extractFeature ./prefix.topk $cur_dir/seq_with_label.dat > prefix.topk.feature
        $bin/extractFeature ./prefix.max $cur_dir/seq_with_label.dat > prefix.max.feature

        #separate for train and test
        $bin/sep.py mcmc.feature mcmc.feature
        $bin/sep.py prefix.topk.feature prefix.topk.feature
        $bin/sep.py prefix.max.feature prefix.max.feature
        # do svm
        # for mcmc
        $bin/svm-train mcmc.featurep80 
        $bin/svm-predict mcmc.featurep20 mcmc.featurep80.model mcmc.predict > mcmc.result
        #for preffix+sample
        $bin/svm-train prefix.max.featurep80 
        $bin/svm-predict prefix.max.featurep20 prefix.max.featurep80.model prefix.max.predict > prefix.max.result
        #for topk
        $bin/svm-train prefix.topk.featurep80 
        $bin/svm-predict prefix.topk.featurep20 prefix.topk.featurep80.model prefix.topk.predict > prefix.topk.result

    done

    #for sup
    # Base
    sup=303030
    k=20

    cd $cur_dir
    mkdir $cur_dir/sup
    for((sup = 20; sup <= 50; sup=sup+5 )) do
        mkdir $cur_dir/sup/$sup
        cd $cur_dir/sup/$sup
        # run mcmc algorithm 
        java -jar $bin/mcmc.jar \
            -f $cur_dir/seq.dat \
            -k $k \
            -min_sup $sup \
            -thread_num 2\
            -item_num $d  > ./mcmc.freq

            #-min_sup `echo "$n*$sup/100"|bc`\
        #run prefixspan
        # $bin/prefixspan -min_sup `echo "$n*$sup/100"|bc` $cur_dir/seq.dat > prefix.ori
        $bin/prefixspan -min_sup $sup $cur_dir/seq.dat > prefix.ori


        #sampling on prefix span
        java -jar $bin/TopkFinder.jar -k $k -f ./prefix.ori > prefix.topk
        java -jar $bin/maxpatsampler.jar -k $k -f ./prefix.ori > prefix.max

        $bin/extractFeature ./mcmc.freq $cur_dir/seq_with_label.dat > mcmc.feature
        $bin/extractFeature ./prefix.topk $cur_dir/seq_with_label.dat > prefix.topk.feature
        $bin/extractFeature ./prefix.max $cur_dir/seq_with_label.dat > prefix.max.feature

        #separate for train and test
        $bin/sep.py mcmc.feature mcmc.feature
        $bin/sep.py prefix.topk.feature prefix.topk.feature
        $bin/sep.py prefix.max.feature prefix.max.feature

        # do svm
        # for mcmc
        $bin/svm-train mcmc.featurep80 
        $bin/svm-predict mcmc.featurep20 mcmc.featurep80.model mcmc.predict > mcmc.result
        #for preffix+sample
        $bin/svm-train prefix.max.featurep80 
        $bin/svm-predict prefix.max.featurep20 prefix.max.featurep80.model prefix.max.predict > prefix.max.result
        #for topk
        $bin/svm-train prefix.topk.featurep80 
        $bin/svm-predict prefix.topk.featurep20 prefix.topk.featurep80.model prefix.topk.predict > prefix.topk.result
    done
done
