package knowledge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.Common;

public class TCMMeSH {

	/**
	 * 返回每一类的所有实体
	 * 
	 * @param files
	 *            每一类文件
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<String>> getClassEntities(File[] files) throws IOException {

		Map<String, Set<String>> descript_map = new HashMap<>();

		for (File file : files) {

			String name = Common.getFileName(file);

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			Set<String> entities = new HashSet<>();

			String line = "";

			while ((line = reader.readLine()) != null) {

				String[] temp = line.split(" ");

				if (temp[0].contains("［"))
					temp[0] = temp[0].substring(0, temp[0].indexOf('［'));

				if (temp[0].contains("（"))
					temp[0] = temp[0].substring(0, temp[0].indexOf('（'));

				if (temp[0].contains("，"))
					temp[0] = temp[0].substring(temp[0].indexOf('，') + 1, temp[0].length());

				entities.add(temp[0]);

			}

			descript_map.put(name, entities);

			reader.close();

		}

		return descript_map;

	}

	/**
	 * 返回每一类的所有实体及描述
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getClassDescriptions(File[] files) throws IOException {

		Map<String, String> descript_map = new HashMap<>();

		for (File file : files) {

			String name = Common.getFileName(file);

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			StringBuilder sb = new StringBuilder();

			String line = "";

			while ((line = reader.readLine()) != null) {

				sb.append(line);

			}

			descript_map.put(name, sb.toString());

			reader.close();

		}

		return descript_map;

	}

	/**
	 * 得到词的ESA向量
	 * 
	 * @param filename
	 * @return @throws IOException @throws
	 */
	public static Map<String, double[]> getWordEsaVec(String filename) throws IOException {

		Map<String, double[]> vec_map = new HashMap<>();

		File f = new File(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line = "";

		while ((line = reader.readLine()) != null) {

			String[] temp = line.split("\t");

			String[] tfidf = temp[1].split(" ");

			double[] vector = new double[tfidf.length];

			for (int i = 0; i < vector.length; i++) {

				vector[i] = Double.parseDouble(tfidf[i]);

			}

			vec_map.put(temp[0], vector);

		}

		reader.close();

		return vec_map;

	}

}
