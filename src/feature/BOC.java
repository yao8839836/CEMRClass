package feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Common;
import util.ReadWriteFile;

public class BOC {

	/**
	 * 
	 * 从语料库文件中获取每个医案的主题词表词是否存在的二进制Bag of Concept向量
	 * 
	 * @param files
	 * @param concepts
	 *            主题词表概念列表
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> binary_vector(File[] files, List<String> concepts) throws IOException {

		Map<String, double[]> vector_map = new HashMap<>();

		int dimension = concepts.size();

		for (File f : files) {

			String content = ReadWriteFile.getClinicalCasesContent(f);

			double[] vector = new double[dimension];

			for (int i = 0; i < dimension; i++) {

				String concept = concepts.get(i);

				if (content.contains(concept)) {

					vector[i] = 1;

				}

			}

			String filename = Common.getFileName(f);

			System.out.println(filename + " " + dimension);

			vector_map.put(filename, vector);
		}

		return vector_map;

	}

	/**
	 * 
	 * 返回映射主题词表对应目录实体次数的向量
	 * 
	 * @param files
	 * @param categories
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> map_count_vector(File[] files, Map<String, Set<String>> categories)
			throws IOException {

		Map<String, double[]> vector_map = new HashMap<>();

		List<String> category_names = new ArrayList<>(categories.keySet());

		for (File file : files) {

			String content = ReadWriteFile.getTextContent(file);

			double[] count = new double[category_names.size()];

			for (String category : categories.keySet()) {

				Set<String> entities = categories.get(category);

				for (String concept : entities) {

					if (content.contains(concept)) {

						count[category_names.indexOf(category)]++;

					}

				}

				String filename = Common.getFileName(file);

				vector_map.put(filename, count);
			}

		}

		return vector_map;

	}

	/**
	 * 
	 * 返回映射主题词表对应目录描述次数的向量
	 * 
	 * @param files
	 * @param categories
	 * @return
	 * @throws IOException
	 */
	public static Map<String, double[]> map_description_count_vector(File[] files, Map<String, String> categories)
			throws IOException {

		Map<String, double[]> vector_map = new HashMap<>();

		List<String> category_names = new ArrayList<>(categories.keySet());

		for (File file : files) {

			String content = ReadWriteFile.getTextContent(file);

			double[] count = new double[category_names.size()];

			for (String category : categories.keySet()) {

				String desc = categories.get(category);

				String[] temp = content.split(" ");

				for (String concept : temp) {

					if (desc.contains(concept)) {

						count[category_names.indexOf(category)]++;

					}

				}

				String filename = Common.getFileName(file);

				vector_map.put(filename, count);
			}

		}

		return vector_map;

	}
}
