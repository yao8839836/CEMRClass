package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import topic.LDA;
import util.Corpus;

public class RunLDA {

	public static void main(String[] args) throws IOException {

		List<String> vocab = Corpus.getVocab("data//vocab.txt");

		int[][] docs = Corpus.getDocuments("data//corpus.txt");

		List<String> doc_id_list = Corpus.getVocab("data//word2vec_doc_id.txt");

		int K = 50;
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

	}

}
