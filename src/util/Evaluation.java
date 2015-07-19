package util;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class Evaluation {

	/**
	 * 计算特征+分类器的 macro averaged F1 score
	 * 
	 * @param classifier
	 * @param test
	 * @return
	 * @throws Exception
	 */
	public static double macro_F1(Classifier classifier, Instances test) throws Exception {

		int num_class = test.numClasses();

		int num_instances = test.numInstances();

		int[] tp = new int[num_class];

		int[] fp = new int[num_class];

		int[] fn = new int[num_class];

		int[] tn = new int[num_class];

		for (int i = 0; i < num_class; i++) {

			for (int j = 0; j < num_instances; j++) {

				Instance test_instance = test.instance(j);

				int real_label = (int) test_instance.classValue();

				double class_value = classifier.classifyInstance(test_instance);

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

		}

		return macro_f1 / num_class;

	}

}
