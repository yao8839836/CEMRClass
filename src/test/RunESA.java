package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import preprocessing.WordSegment;
import util.Corpus;
import util.ReadWriteFile;

public class RunESA {

	public static void main(String[] args) throws Exception {

		List<String> vocab = Corpus.getVocab("data//vocab.txt");

		int[][] docs = Corpus.getDocumentsWithOutLabel("data//esa_corpus_small.txt");

		Set<Integer> word_set = new HashSet<>();

		for (int[] doc : docs) {

			for (int word : doc) {

				word_set.add(word);

			}

		}

		StringBuilder sb = new StringBuilder(word_set.size() + " " + docs.length + "\n");

		for (int e : word_set) {

			double idf = Corpus.IDF(docs, e);

			StringBuilder word_vec = new StringBuilder(vocab.get(e) + " ");

			for (int[] doc : docs) {

				int tf = Corpus.TF(doc, e);

				double tfidf = tf * idf;

				word_vec.append(tfidf + " ");

			}
			sb.append(word_vec.toString().trim() + "\n");

		}

		ReadWriteFile.writeFile("file//esa_small.txt", sb.toString());

	}

	/**
	 * 得到每个概念的分词结果
	 * 
	 * @param filename
	 * @param vocab
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getConceptDesc(String filename, List<String> vocab) throws Exception {

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

		WordSegment ws = new WordSegment();

		String line = "";

		Map<String, String> desc = new HashMap<>();

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split("\t");

			if (temp.length == 2) {

				String words = ws.segment(temp[1]);

				String[] word = words.split(" ");

				StringBuilder sb = new StringBuilder();

				for (String token : word) {

					if (vocab.contains(token))

						sb.append(token + " ");

				}

				String desc_words = sb.toString().trim();

				System.out.println(temp[0] + "\t" + desc_words);

				desc.put(temp[0], desc_words);

			}

		}

		reader.close();

		return desc;
	}
}
