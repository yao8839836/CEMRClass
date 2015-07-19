package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import preprocessing.StopRemove;
import util.Common;
import util.ReadWriteFile;

public class RunStopRemove {

	public static void main(String[] args) throws IOException {

		String filename = "models//stopwords//StopWords.txt";

		Set<String> stop_words = StopRemove.getStopWords(filename);

		File[] files = new File(Common.segment_fold).listFiles();

		Map<String, Integer> word_count = StopRemove.removeStopWords(files, stop_words);

		files = new File(Common.stop_remove_fold).listFiles();

		List<String> vocab = StopRemove.removeRareWords(files, word_count, 10);

		StringBuilder sb = new StringBuilder();

		for (String word : vocab) {
			sb.append(word + "\n");
		}

		ReadWriteFile.writeFile("data//vocab.txt", sb.toString().trim());

	}

}
