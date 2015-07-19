package feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import util.Corpus;

public class BOW {

	/**
	 * 从语料库文件中获取每个医案的词是否存在的二进制BOW向量
	 * 
	 * @param filename
	 * @param V
	 *            词表大小
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> binary_vector(String filename, int V) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		Map<String, double[]> vector_map = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");

			double[] vector = new double[V];

			String[] words = temp[temp.length - 1].split(" ");

			for (String word : words) {

				int index = Integer.parseInt(word);

				vector[index] = 1;

			}

			vector_map.put(temp[0], vector);

		}

		reader.close();

		return vector_map;

	}

	/**
	 * 从语料库文件中每个医案的ifidf BOW向量
	 * 
	 * @param filename
	 * @param V
	 *            词表大小
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> tfidf_vector(String filename, int V) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		int[][] docs = Corpus.getDocuments(filename);

		// idf向量
		double[] idf = Corpus.IDF_Array(V, docs);

		Map<String, double[]> vector_map = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");

			double[] vector = new double[V];

			String[] words = temp[temp.length - 1].split(" ");

			for (String word : words) {

				int index = Integer.parseInt(word);

				vector[index] += idf[index];
			}

			vector_map.put(temp[0], vector);

		}

		reader.close();

		return vector_map;

	}
}
