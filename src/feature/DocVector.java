package feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Common;
import util.Corpus;
import wordvector.Word2Vec;

public class DocVector {

	/**
	 * @param doc_id_list
	 *            文件名列表
	 * @param filename
	 *            文本向量文件
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> getDocVector(List<String> doc_id_list, String filename) throws IOException {

		Word2Vec doc_vec = new Word2Vec(filename);

		Map<String, double[]> vector_map = new HashMap<>();

		// 词、文本向量文件格式一致
		Map<String, double[]> line_vector = doc_vec.getWordVector();

		for (String id : doc_id_list) {

			int line_no = doc_id_list.indexOf(id);

			vector_map.put(id, line_vector.get("sent_" + line_no));

		}

		return vector_map;
	}

	/**
	 * 词向量TFIDF加权作为文本向量
	 * 
	 * @param w2v_file
	 *            词向量文件
	 * @param corpus_file
	 *            语料库文件
	 * @param vocab_file
	 *            词表文件
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> getWordVectorAverage(String w2v_file, String corpus_file, String vocab_file)
			throws IOException {

		Map<String, double[]> vector_map = new HashMap<>();

		Word2Vec w2v = new Word2Vec(w2v_file);

		// 词向量
		Map<String, double[]> word_vector_map = w2v.getWordVector();

		int[][] docs = Corpus.getDocuments(corpus_file);

		List<String> vocab = Corpus.getVocab(vocab_file);

		// idf向量
		double[] idf = Corpus.IDF_Array(vocab.size(), docs);

		File f = new File(corpus_file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");

			String[] words = temp[temp.length - 1].split(" ");

			int index_0 = Integer.parseInt(words[0]);

			double[] doc_vec = new double[word_vector_map.get(vocab.get(index_0)).length];

			for (String word : words) {

				int index = Integer.parseInt(word);

				double[] word_vec = word_vector_map.get(vocab.get(index));

				for (int i = 0; i < word_vec.length; i++) {

					doc_vec[i] += (idf[index] * word_vec[i] / words.length);

					// doc_vec[i] += (word_vec[i] / words.length);

				}

			}

			vector_map.put(temp[0], doc_vec);

		}

		reader.close();

		return vector_map;

	}

	/**
	 * 
	 * 将医案中出现的概念的词向量相加、平均
	 * 
	 * @param w2v_file
	 *            词向量文件
	 * @param files
	 *            每篇医案概念文件
	 * @param dimension
	 *            词向量维度
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> getConceptVectorAverage(String w2v_file, File[] files, int dimension)
			throws IOException {

		Map<String, double[]> vector_map = new HashMap<>();

		Word2Vec w2v = new Word2Vec(w2v_file);

		// 词向量
		Map<String, double[]> word_vector_map = w2v.getWordVector();

		for (File file : files) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			String concepts = reader.readLine();

			reader.close();

			double[] vector = new double[dimension];

			String[] temp = concepts.split(" ");

			for (String concept : temp) {

				if (word_vector_map.containsKey(concept)) {

					double[] word_vec = word_vector_map.get(concept);

					for (int i = 0; i < dimension; i++) {

						vector[i] += (word_vec[i] / temp.length);

					}

				}

			}

			String filename = Common.getFileName(file);

			vector_map.put(filename, vector);

		}

		return vector_map;

	}
}
