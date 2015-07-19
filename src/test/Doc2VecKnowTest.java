package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import feature.DocVector;
import preprocessing.BuildInstances;
import util.Corpus;
import util.ReadWriteFile;
import wordvector.LearnDocVec;
import wordvector.LearnWithKnow;
import wordvector.Neuron;
import wordvector.Word2Vec;
import wordvector.WordEntry;

public class Doc2VecKnowTest {

	public static void main(String[] args) throws IOException {

		Word2Vec word_esa = new Word2Vec("file//esa_small.txt");

		Map<String, Map<String, Double>> mustlinks = new HashMap<>();

		Map<String, double[]> word_esa_vector = word_esa.getWordVector();

		for (String word : word_esa_vector.keySet()) {

			Map<String, Double> mustlink = new HashMap<>();

			// ESA相似的词
			Set<WordEntry> word_set = word_esa.distance(word);

			for (WordEntry sim_word : word_set) {

				String name = sim_word.name;

				double score = sim_word.score;

				if (!word.equals(name)) {

					mustlink.put(name, score);

					System.out.println(word + "\t" + name + "\t" + score);

				}

			}

			mustlinks.put(word, mustlink);

		}
		System.out.println(mustlinks.keySet().size());

		File result = new File("file//clinicalcases.txt");

		// 训练带知识的词向量

		LearnWithKnow learn = new LearnWithKnow(mustlinks);

		learn.learnFile(result);

		learn.saveModel(new File("models//clinical.mod"));

		Word2Vec w2v = new Word2Vec();

		w2v.loadJavaModel("models//clinical.mod");

		System.out.println(w2v.distance("麻黄"));

		System.out.println(w2v.analogy("甘草", "咳嗽", "麻黄"));

		/*
		 * 词向量加权试试
		 */

		Map<String, double[]> word_vector = w2v.getWordVector();

		StringBuilder sb = new StringBuilder(word_vector.keySet().size() + " 200\n");

		for (String word : word_vector.keySet()) {

			StringBuilder doc = new StringBuilder(word + " ");

			double[] vector = word_vector.get(word);

			for (double e : vector) {

				doc.append(e + " ");
			}
			sb.append(doc.toString().trim() + "\n");

		}
		ReadWriteFile.writeFile("file//clinical_word_know.vec", sb.toString());

		Map<String, Neuron> word2vec_model = learn.getWord2VecModel();

		/*
		 * 文本向量
		 */

		LearnDocVec learn_doc = new LearnDocVec(word2vec_model);

		learn_doc.learnFile(result);

		Map<Integer, float[]> doc_vec = learn_doc.getDocVector();

		sb = new StringBuilder("7037 200\n");

		for (int doc_no : doc_vec.keySet()) {

			StringBuilder doc = new StringBuilder("sent_" + doc_no + " ");

			float[] vector = doc_vec.get(doc_no);

			for (float e : vector) {

				doc.append(e + " ");
			}
			sb.append(doc.toString().trim() + "\n");

		}
		ReadWriteFile.writeFile("file//clinical_doc_200_type_know.vec", sb.toString());

		/*
		 * 词向量加权写入Weka文件
		 */

		Map<String, Integer> doc_label = Corpus.getDocLabel("data//label.txt");

		Map<String, String> doc_train_test = Corpus.getDocTrainOrTest("data//train_or_test.txt");

		int V = 200;

		Map<String, double[]> vector_map = DocVector.getWordVectorAverage("file//clinical_word_know.vec",
				"data//corpus_seg.txt", "data//vocab_all.txt");

		String training_data = BuildInstances.getTrainingSet(doc_label, doc_train_test, vector_map, V);

		String test_data = BuildInstances.getTestSet(doc_label, doc_train_test, vector_map, V);

		ReadWriteFile.writeFile("file//train_w2v_know_tfidf.arff", training_data);

		ReadWriteFile.writeFile("file//test_w2v_know_tfidf.arff", test_data);

		/*
		 * 文本向量写入Weka文件
		 */

		List<String> doc_id_list = Corpus.getVocab("data//word2vec_doc_id.txt");

		vector_map = DocVector.getDocVector(doc_id_list, "file//clinical_doc_200_type_know.vec");

		training_data = BuildInstances.getTrainingSet(doc_label, doc_train_test, vector_map, V);

		test_data = BuildInstances.getTestSet(doc_label, doc_train_test, vector_map, V);

		ReadWriteFile.writeFile("file//train_doc2vec_200_type_know.arff", training_data);

		ReadWriteFile.writeFile("file//test_doc2vec_200_type_know.arff", test_data);

	}
}
