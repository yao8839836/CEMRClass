package feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESA {

	/**
	 * 根据词的ESA向量得到文本的ESA向量
	 * 
	 * @param word_esa
	 * @param docs
	 * @param vocab
	 * @return
	 */
	public static Map<String, double[]> getDocESA(Map<String, double[]> word_esa, Map<String, int[]> docs,
			List<String> vocab) {

		Map<String, double[]> doc_vector = new HashMap<>();

		/*
		 * 维度，等于概念的数量
		 */
		int dimension = 0;

		for (String word : word_esa.keySet()) {

			dimension = word_esa.get(word).length;

			break;

		}

		for (String doc : docs.keySet()) {

			double[] vector = new double[dimension];

			int[] doc_words = docs.get(doc);

			for (int word : doc_words) {

				String word_str = vocab.get(word);

				if (word_esa.containsKey(word_str)) {

					double[] word_vector = word_esa.get(word_str);

					for (int i = 0; i < dimension; i++) {

						vector[i] += (word_vector[i] / doc_words.length);

					}

				}

			}

			doc_vector.put(doc, vector);

		}

		return doc_vector;
	}

}
