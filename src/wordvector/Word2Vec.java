package wordvector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Word2Vec {

	Map<String, double[]> wordMap;

	int topNSize = 100;

	private int words;
	private int size;

	/**
	 * 构造方法,从文件中得到词向量
	 * 
	 * @param filename
	 *            存储词向量的文件
	 * @throws IOException
	 */
	public Word2Vec(String filename) throws IOException {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		reader.readLine();

		wordMap = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split(" ");

			double[] vector = new double[temp.length - 1];

			for (int i = 0; i < vector.length; i++) {

				vector[i] = Double.parseDouble(temp[i + 1]);
			}

			wordMap.put(temp[0], vector);

		}

		reader.close();

	}

	/**
	 * 默认构造方法
	 */
	public Word2Vec() {

		wordMap = new HashMap<>();

	}

	/**
	 * 返回与查询词余弦距离最近的词
	 * 
	 * @param queryWord
	 *            查询词
	 * @return 相近词及其余弦相似度的集合
	 */
	public Set<WordEntry> distance(String queryWord) {

		double[] center = wordMap.get(queryWord);
		if (center == null) {
			return Collections.emptySet();
		}

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double norm = 0;
		for (int i = 0; i < center.length; i++) {
			norm += center[i] * center[i];
		}
		norm = Math.sqrt(norm);

		double min = Double.MIN_VALUE;

		for (Map.Entry<String, double[]> entry : wordMap.entrySet()) {

			double[] vector = entry.getValue();
			double dist = 0;
			for (int i = 0; i < vector.length; i++) {
				dist += center[i] * vector[i];
			}
			double norm1 = 0;
			for (int i = 0; i < vector.length; i++) {

				norm1 += vector[i] * vector[i];
			}
			norm1 = Math.sqrt(norm1);
			dist = (double) (dist / (norm * norm1));
			if (dist > min) {
				result.add(new WordEntry(entry.getKey(), dist));
				if (resultSize < result.size()) {
					result.pollLast();
				}
				min = result.last().score;
			}
		}
		result.pollFirst();// 本身

		return result;
	}

	/**
	 * 类比
	 * 
	 * @param word0
	 * @param word1
	 * @param word2
	 * @return
	 */
	public Set<WordEntry> analogy(String word0, String word1, String word2) {
		double[] wv0 = getWordVector(word0);
		double[] wv1 = getWordVector(word1);
		double[] wv2 = getWordVector(word2);

		if (wv1 == null || wv2 == null || wv0 == null) {
			return null;
		}
		double[] wordVector = new double[size];
		for (int i = 0; i < size; i++) {
			wordVector[i] = wv1[i] - wv0[i] + wv2[i];
		}
		double[] tempVector;
		String name;
		List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);
		for (Entry<String, double[]> entry : wordMap.entrySet()) {
			name = entry.getKey();
			if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {
				continue;
			}
			float dist = 0;
			tempVector = entry.getValue();
			for (int i = 0; i < wordVector.length; i++) {
				dist += wordVector[i] * tempVector[i];
			}
			insertTopN(name, dist, wordEntrys);
		}
		return new TreeSet<WordEntry>(wordEntrys);
	}

	/**
	 * 
	 * 类比最相似列表
	 * 
	 * @param name
	 * @param score
	 * @param wordsEntrys
	 */
	private void insertTopN(String name, float score, List<WordEntry> wordsEntrys) {
		// TODO Auto-generated method stub
		if (wordsEntrys.size() < topNSize) {
			wordsEntrys.add(new WordEntry(name, score));
			return;
		}
		float min = Float.MAX_VALUE;
		int minOffe = 0;
		for (int i = 0; i < topNSize; i++) {
			WordEntry wordEntry = wordsEntrys.get(i);
			if (min > wordEntry.score) {
				min = (float) wordEntry.score;
				minOffe = i;
			}
		}

		if (score > min) {
			wordsEntrys.set(minOffe, new WordEntry(name, score));
		}

	}

	/**
	 * 获取词向量表
	 * 
	 * @return 词向量表
	 */
	public Map<String, double[]> getWordVector() {

		return wordMap;
	}

	/**
	 * 得到词向量
	 * 
	 * @param word
	 * @return
	 */
	public double[] getWordVector(String word) {
		return wordMap.get(word);
	}

	/**
	 * 加载模型
	 * 
	 * @param path
	 *            模型的路径
	 * @throws IOException
	 */
	public void loadJavaModel(String path) throws IOException {
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
			words = dis.readInt();
			size = dis.readInt();

			float vector = 0;

			String key = "";
			double[] value = new double[size];
			for (int i = 0; i < words; i++) {
				double len = 0;
				key = dis.readUTF();
				value = new double[size];
				for (int j = 0; j < size; j++) {
					vector = dis.readFloat();
					len += vector * vector;
					value[j] = vector;
				}

				len = Math.sqrt(len);

				for (int j = 0; j < size; j++) {
					value[j] /= len;
				}
				wordMap.put(key, value);
			}
			dis.close();
		}
	}

}
