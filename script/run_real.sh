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
