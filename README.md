# CEMRClass
Traditional Chinese Medicine Clinical Records Classification, In IEEE BIBM 2016

# DataSet

Clinical records are in data/ClinicalCases/

data/train_or_test.txt : indicating each record belongs to training set or test set.

data/label.txt : indicating each record's category label.

# Require

Java 7 or above (I use Java 8); Eclipse

# Usage

1. Run src/test/RunESA.java to generate ESA file.
2. Run src/test/Doc2VecKnowTest.java to generate TCM knowledge-based doc2vec features.
3. Run src/test/Predict.java to use classifiers to predict clinical records' category.
