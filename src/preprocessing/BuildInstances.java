package preprocessing;

import java.util.Map;

public class BuildInstances {

	/**
	 * 从特征、label、训练\测试 关联数组构造训练集
	 * 
	 * @param doc_label
	 * @param doc_train_test
	 * @param vector_map
	 * @param dimension
	 * @return
	 */
	public static String getTrainingSet(Map<String, Integer> doc_label, Map<String, String> doc_train_test,
			Map<String, double[]> vector_map, int dimension) {

		StringBuilder sb = new StringBuilder("@relation EMR\n");

		for (int i = 0; i < dimension; i++)
			sb.append("@attribute feature_" + i + " numeric\n");
		sb.append("@attribute 'class' {0, 1, 3, 5, 6}\n\n@data\n");

		for (String doc : vector_map.keySet()) {

			double[] vector = vector_map.get(doc);

			if (doc_train_test.get(doc).equals("train")) {

				for (double e : vector) {

					sb.append(e + ",");

				}
				sb.append(doc_label.get(doc) + "\n");

			}

		}

		return sb.toString();
	}

	/**
	 * 从特征、label、训练\测试 关联数组构造测试集
	 * 
	 * @param doc_label
	 * @param doc_train_test
	 * @param vector_map
	 * @param dimension
	 * @return
	 */
	public static String getTestSet(Map<String, Integer> doc_label, Map<String, String> doc_train_test,
			Map<String, double[]> vector_map, int dimension) {

		StringBuilder sb = new StringBuilder("@relation EMR\n");

		for (int i = 0; i < dimension; i++)
			sb.append("@attribute feature_" + i + " numeric\n");
		sb.append("@attribute 'class' {0, 1, 3, 5, 6}\n\n@data\n");

		for (String doc : vector_map.keySet()) {

			double[] vector = vector_map.get(doc);

			if (doc_train_test.get(doc).equals("test")) {

				for (double e : vector) {

					sb.append(e + ",");

				}
				sb.append(doc_label.get(doc) + "\n");

			}

		}

		return sb.toString();
	}

}
