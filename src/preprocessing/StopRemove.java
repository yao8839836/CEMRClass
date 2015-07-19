package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Common;
import util.ReadWriteFile;

public class StopRemove {

	/**
	 * 从文件中读取停用词集合
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getStopWords(String filename) throws IOException {

		Set<String> stop_words = new HashSet<>();

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		while ((line = reader.readLine()) != null) {
			stop_words.add(line);
		}
		reader.close();

		return stop_words;

	}

	/**
	 * 将文本去除停用词，保存文件、返回词计数器
	 * 
	 * @param files
	 *            分完词的文件列表
	 * @param stop_words
	 *            停止词列表
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Integer> removeStopWords(File[] files, Set<String> stop_words) throws IOException {

		Map<String, Integer> word_count = new HashMap<>();

		for (File f : files) {

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				String[] words = line.split(" ");

				for (String token : words) {

					if (!stop_words.contains(token)) {

						sb.append(token + " ");

						if (word_count.containsKey(token))
							word_count.put(token, word_count.get(token) + 1);
						else
							word_count.put(token, 1);

					}

				}
			}
			reader.close();

			String filename = Common.getFileName(f);

			ReadWriteFile.writeFile(Common.stop_remove_fold + filename + ".txt", sb.toString().trim());

		}

		return word_count;

	}

	/**
	 * 去除文本中的罕见词，得到词表
	 * 
	 * @param files
	 *            去除停用词后的文件列表
	 * @param word_count
	 *            词计数器
	 * @param threshold
	 *            频次阈值
	 * @return
	 * @throws IOException
	 */
	public static List<String> removeRareWords(File[] files, Map<String, Integer> word_count, int threshold)
			throws IOException {

		/*
		 * 写文件
		 */

		for (File f : files) {

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				String[] words = line.split(" ");

				for (String token : words) {

					if (word_count.get(token) >= threshold)
						sb.append(token + " ");

				}

			}

			reader.close();

			String filename = Common.getFileName(f);

			ReadWriteFile.writeFile(Common.rare_remove_fold + filename + ".txt", sb.toString().trim());

		}
		/*
		 * 得到语料库的词表
		 */
		List<String> vocab = new ArrayList<String>();

		for (String word : word_count.keySet()) {

			if (word_count.get(word) >= threshold)
				vocab.add(word);

		}

		return vocab;
	}

}
