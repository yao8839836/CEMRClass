package test;

import java.io.IOException;
import java.util.List;

import util.Corpus;
import util.ReadWriteFile;

public class GenerateWord2VecFile {

	public static void main(String[] args) throws IOException {

		List<String> vocab = Corpus.getVocab("data//vocab.txt");

		int[][] docs = Corpus.getDocuments("data//corpus.txt");

		StringBuilder sb = new StringBuilder();

		for (int[] doc : docs) {

			for (int word : doc) {
				sb.append(vocab.get(word) + " ");
			}
			sb.append("\n");

		}

		ReadWriteFile.writeFile("file//clinicalcases_6699.txt", sb.toString());

	}
}
