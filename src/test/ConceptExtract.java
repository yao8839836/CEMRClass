package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import util.Common;
import util.ReadWriteFile;

public class ConceptExtract {

	public static void main(String[] args) throws IOException {

		/*
		 * 读所有数据，抽取属于主题词表的词
		 */

		Set<String> concepts = ReadWriteFile.getWordSet("data//disease_tcm_mesh.txt");

		ReadWriteFile.writeList(new ArrayList<>(concepts), "data//disease_tcm_mesh.txt");

		File[] files = new File("data//ClinicalCases//").listFiles();

		for (File f : files) {

			String content = ReadWriteFile.getClinicalCasesContent(f);

			StringBuilder sb = new StringBuilder();

			for (String concept : concepts) {

				if (content.contains(concept)) {

					sb.append(concept + " ");
				}

			}

			String filename = Common.getFileName(f);

			System.out.println(filename);

			ReadWriteFile.writeFile(Common.concept_extract_fold + filename + ".txt", sb.toString().trim());

		}

	}

}
