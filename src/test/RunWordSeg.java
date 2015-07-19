package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import preprocessing.WordSegment;
import util.Common;
import util.ReadWriteFile;

public class RunWordSeg {

	public static void main(String[] args) throws Exception {

		WordSegment ws = new WordSegment();

		/*
		 * 读所有数据，分词
		 */

		File[] files = new File("data//ClinicalCases//").listFiles();

		for (File f : files) {

			System.out.println(f);

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			reader.readLine();

			String words = "";

			while ((line = reader.readLine()) != null) {

				words = ws.segment(line);

				System.out.println(words);
			}
			reader.close();

			String filename = Common.getFileName(f);

			System.out.println(filename);

			ReadWriteFile.writeFile(Common.segment_fold + filename + ".txt", words);

		}

	}
}
