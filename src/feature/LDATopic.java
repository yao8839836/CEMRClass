package feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import topic.LDA;
import util.Corpus;

public class LDATopic {

	/**
	 * @param vocab_file
	 *            词表文件
	 * @param corpus_file
	 *            语料库文件
	 * @param doc_id_file
	 *            文件id文件
	 * @param K
	 *            主题数
	 * @return
	 * @throws IOException
	 */

	public static Map<String, double[]> getTopicVector(String vocab_file, String corpus_file, String doc_id_file, int K)
			throws IOException {

		List<String> vocab = Corpus.getVocab(vocab_file);

		int[][] docs = Corpus.getDocuments(corpus_file);

		List<String> doc_id_list = Corpus.getVocab(doc_id_file);

		double alpha = (double) 50 / K;
		double beta = 0.1;
		int iterations = 1000;

		int V = vocab.size();

		LDA lda = new LDA(docs, V);

		lda.markovChain(K, alpha, beta, iterations);

		double[][] theta = lda.estimateTheta();

		Map<String, double[]> vector_map = new HashMap<>();

		for (String doc : doc_id_list) {

			vector_map.put(doc, theta[doc_id_list.indexOf(doc)]);

		}

		return vector_map;

	}

}
