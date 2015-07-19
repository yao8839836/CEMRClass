package test;

import java.io.File;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KDtreeKNN;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class RunKNN {

	/*
	 * 用Java ML的kNN分类
	 */

	public static void main(String[] args) throws Exception {

		Dataset train = ARFFHandler.loadARFF(new File("file//train_doc2vec.arff"), 100);
		System.out.println(train);

		/*
		 * Contruct a KNN classifier that uses 5 neighbors to make a decision.
		 */
		Classifier knn = new KDtreeKNN(5);
		knn.buildClassifier(train);

		/*
		 * Load a data set for evaluation, this can be a different one, but we
		 * will use the same one.
		 */
		Dataset dataForClassification = ARFFHandler.loadARFF(new File("file//test_doc2vec.arff"), 100);

		double f1 = macro_F1(knn, dataForClassification);

		System.out.println("kNN: " + f1);

	}

	/**
	 * 计算特征+分类器的 macro averaged F1 score
	 * 
	 * @param classifier
	 * @param test
	 * @return
	 * @throws Exception
	 */
	public static double macro_F1(Classifier classifier, Dataset test) throws Exception {

		int num_class = test.classes().size();

		int num_instances = test.size();

		int[] tp = new int[num_class];

		int[] fp = new int[num_class];

		int[] fn = new int[num_class];

		int[] tn = new int[num_class];

		for (int i = 0; i < num_class; i++) {

			for (int j = 0; j < num_instances; j++) {

				Instance test_instance = test.get(j);

				int real_label = Integer.parseInt((String) test_instance.classValue());

				int class_value = Integer.parseInt((String) classifier.classify(test_instance));

				int predict_result = (int) class_value;

				if (predict_result == real_label && real_label == i)
					tp[i]++;
				else if (predict_result == real_label && real_label != i)
					tn[i]++;
				else if (predict_result != real_label && real_label == i)
					fn[i]++;
				else if (predict_result != real_label && predict_result == i)
					fp[i]++;

			}

		}

		double macro_f1 = 0;

		for (int i = 0; i < num_class; i++) {

			double precision = (double) tp[i] / (tp[i] + fp[i]);

			double recall = (double) tp[i] / (tp[i] + fn[i]);

			double f1 = (2 * precision * recall) / (precision + recall);

			macro_f1 += f1;

			System.out.println(f1);

		}

		return macro_f1 / num_class;

	}

}
