package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Common {

	/**
	 * 分完词后的文件夹
	 */
	public static final String segment_fold = "file//segment//";
	/**
	 * 去除停用词后的文件目录
	 */
	public static final String stop_remove_fold = "file//stop_remove//";

	/**
	 * 去除罕见词后的文件目录
	 */
	public static final String rare_remove_fold = "file//rare_remove//";

	/**
	 * 抽取主题词表概念后的文件目录
	 */
	public static final String concept_extract_fold = "file//concept_extract//";

	/**
	 * 返回数组中最大元素的下标
	 * 
	 * @param array
	 *            输入数组
	 * @return 最大元素的下标
	 */
	public static int maxIndex(double[] array) {
		double max = array[0];
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}

		}
		return maxIndex;

	}

	/**
	 * 返回数组中最小元素的下标
	 * 
	 * @param array
	 *            输入数组
	 * @return 最小元素的下标
	 */
	public static int minIndex(double[] array) {
		double min = array[0];
		int minIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}

		}
		return minIndex;

	}

	/**
	 * 返回数组中的最小值
	 * 
	 * @param array
	 *            输入数组
	 * @return
	 */
	public static double min(double[] array) {

		double min = array[0];

		for (int i = 0; i < array.length; i++) {
			if (array[i] < min)
				min = array[i];
		}

		return min;

	}

	/**
	 * 获取文件对象的文件名
	 * 
	 * @param file
	 *            文件对象
	 * @return
	 */
	public static String getFileName(File file) {

		String file_str = file.toString();

		String filename = file_str.substring(file_str.lastIndexOf("\\") + 1, file_str.lastIndexOf("."));

		return filename;
	}

	/**
	 * 将两个特征向量拼接起来
	 * 
	 * @param vector_1
	 * @param vector_2
	 * @return
	 */
	public static Map<String, double[]> append_feature_vector(Map<String, double[]> vector_1,
			Map<String, double[]> vector_2) {

		Map<String, double[]> vector = new HashMap<>();

		for (String name : vector_1.keySet()) {

			double[] f_1 = vector_1.get(name);

			double[] f_2 = vector_2.get(name);

			double[] f = new double[f_1.length + f_2.length];

			for (int i = 0; i < f_1.length; i++) {

				f[i] = f_1[i];

			}

			for (int j = 0; j < f_2.length; j++) {

				f[f_1.length + j] = f_2[j];

			}

			vector.put(name, f);

		}

		return vector;
	}

}
