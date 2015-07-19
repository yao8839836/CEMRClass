package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import feature.DocVector;
import preprocessing.BuildInstances;
import util.Corpus;
import util.ReadWriteFile;
import wordvector.Learn;
import wordvector.LearnDocVec;
import wordvector.Neuron;
import wordvector.Word2Vec;

public class Doc2VecTest {

	public static void main(String[] args) throws IOException {

		File result = new File("file//clinicalcases.txt");

		Learn learn = new Learn();

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
		ReadWriteFile.writeFile("file//clinical_word_java.vec", sb.toString());

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
		ReadWriteFile.writeFile("file//clinical_doc_200_java.vec", sb.toString());

		Map<String, Integer> doc_label = Corpus.getDocLabel("data//label.txt");

		Map<String, String> doc_train_test = Corpus.getDocTrainOrTest("data//train_or_test.txt");

		int V = 200;

		List<String> doc_id_list = Corpus.getVocab("data//word2vec_doc_id.txt");

		Map<String, double[]> vector_map = DocVector.getDocVector(doc_id_list, "file//clinical_doc_200_java.vec");

		String training_data = BuildInstances.getTrainingSet(doc_label, doc_train_test, vector_map, V);

		String test_data = BuildInstances.getTestSet(doc_label, doc_train_test, vector_map, V);

		ReadWriteFile.writeFile("file//train_doc2vec_200_java.arff", training_data);

		ReadWriteFile.writeFile("file//test_doc2vec_200_java.arff", test_data);

	}

}
