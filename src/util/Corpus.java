package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Liang Yao
 * @email yaoliangzju@gmail.com
 *
 */
public class Corpus {

	/**
	 * 读取词表，一行一个词
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> getVocab(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		List<String> vocab = new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			vocab.add(line);
		}
		reader.close();
		return vocab;
	}

	/**
	 * 读取文档集，一行一篇文章, 有标签
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int[][] getDocuments(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		List<String> docsLines = new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			if (line.trim().length() > 0)
				docsLines.add(line);
		}
		reader.close();

		int[][] docs = new int[docsLines.size()][];

		for (int d = 0; d < docs.length; d++) {

			String doc = docsLines.get(d);

			String[] temp = doc.split(" : ");

			String doc_content = temp[temp.length - 1];
			String[] tokens = doc_content.trim().split(" ");

			docs[d] = new int[tokens.length];

			for (int n = 0; n < tokens.length; n++) {
				int wordid = Integer.parseInt(tokens[n]);
				docs[d][n] = wordid;
			}

		}

		return docs;
	}

	/**
	 * 读取文档集，一行一篇文章, 无标签
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int[][] getDocumentsWithOutLabel(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		List<String> docsLines = new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			if (line.trim().length() > 0)
				docsLines.add(line);
		}
		reader.close();

		int[][] docs = new int[docsLines.size()][];

		for (int d = 0; d < docs.length; d++) {

			String doc = docsLines.get(d);
			String[] tokens = doc.trim().split(" ");

			docs[d] = new int[tokens.length];

			for (int n = 0; n < tokens.length; n++) {
				int wordid = Integer.parseInt(tokens[n]);
				docs[d][n] = wordid;
			}

		}

		return docs;
	}

	/**
	 * 统计词在文档中的词频
	 * 
	 * @param documents
	 * @param word
	 * @return
	 */
	public static int TF(int[] document, int word) {
		int count = 0;
		for (int e : document) {
			if (e == word)
				count++;
		}
		return count;
	}

	/**
	 * 从文件中读LDA格式的语料库(文件名-内容)
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 * 
	 */
	public static Map<String, int[]> getDocumentsMap(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		Map<String, int[]> doc_map = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");

			String doc_id = temp[0];

			String doc_content = temp[temp.length - 1];

			String[] tokens = doc_content.split(" ");

			int[] doc = new int[tokens.length];

			for (int n = 0; n < tokens.length; n++) {

				doc[n] = Integer.parseInt(tokens[n]);

			}

			doc_map.put(doc_id, doc);

		}

		reader.close();

		return doc_map;

	}

	/**
	 * 从文件中读取医案的label
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Integer> getDocLabel(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		Map<String, Integer> doc_label = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");
			doc_label.put(temp[0], Integer.parseInt(temp[1]));
		}

		reader.close();

		return doc_label;
	}

	/**
	 * 从文件中读取医案属于训练集还是测试集
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getDocTrainOrTest(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		Map<String, String> doc_train_test = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" : ");
			doc_train_test.put(temp[0], temp[1]);
		}

		reader.close();

		return doc_train_test;
	}

	/**
	 * 获取一个词在文档集中的IDF
	 * 
	 * @param documents
	 * @param word
	 * @return
	 */
	public static double IDF(int[][] documents, int word) {
		int count = 0;

		for (int[] document : documents) {

			for (int e : document) {
				if (e == word) {
					count++;
					break;
				}

			}
		}
		return Math.log((double) documents.length / count);
	}

	/**
	 * @param V
	 *            词表大小
	 * @param docs
	 *            语料库
	 * @return
	 */
	public static double[] IDF_Array(int V, int[][] docs) {

		double[] idf = new double[V];

		for (int v = 0; v < V; v++) {

			idf[v] = IDF(docs, v);

		}

		return idf;
	}

}
