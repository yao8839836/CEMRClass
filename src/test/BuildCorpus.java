package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.Common;
import util.Corpus;
import util.ReadWriteFile;
import wordvector.Word2Vec;

public class BuildCorpus {

	public static void main(String[] args) throws IOException {

		File[] files = new File(Common.segment_fold).listFiles();

		Word2Vec w2v = new Word2Vec("file//clinical_word.vec");

		Map<String, double[]> word_vector = w2v.getWordVector();

		List<String> vocab = new ArrayList<>(word_vector.keySet());

		ReadWriteFile.writeList(vocab, "data//vocab_all.txt");

		Map<String, Integer> doc_label = Corpus.getDocLabel("data//label.txt");

		Map<String, String> doc_train_test = Corpus.getDocTrainOrTest("data//train_or_test.txt");

		StringBuilder sb = new StringBuilder();

		for (File f : files) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String line = reader.readLine();

			reader.close();

			String[] words = line.split(" ");

			String filename = Common.getFileName(f);

			int label = doc_label.get(filename);

			String train_test = doc_train_test.get(filename);

			sb.append(filename + " : " + train_test + " : " + label + " : ");

			StringBuilder doc = new StringBuilder();

			for (String word : words) {

				if (vocab.indexOf(word) != -1)
					doc.append(vocab.indexOf(word) + " ");
			}
			sb.append(doc.toString().trim() + "\n");

		}

		ReadWriteFile.writeFile("data//corpus_seg.txt", sb.toString());

	}
}
