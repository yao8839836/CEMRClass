# CEMRClass

The dataset and code of our paper:

Yao, L., Zhang, Y., Wei, B., Li, Z., & Huang, X. (2016, December). Traditional Chinese medicine clinical records classification using knowledge-powered document embedding. In Bioinformatics and Biomedicine (BIBM), 2016 IEEE International Conference on (pp. 1926-1928). IEEE.

# DataSet

The Copyright of the dataset used in the paper belongs to [China Knowledge
Centre for Engineering Sciences and Technology](http://zcy.ckcest.cn/tcm/). The dataset is for research use only.

Clinical records are in data/ClinicalCases/

data/train_or_test.txt : indicating each record belongs to training set or test set.

data/label.txt : indicating each record's category label.

# Require

Java 7 or above (I use Java 8); Eclipse

# Usage

1. Run src/test/RunESA.java to generate ESA file.
2. Run src/test/Doc2VecKnowTest.java to generate TCM knowledge-based doc2vec features.
3. Run src/test/Predict.java to use classifiers to predict clinical records' category.
