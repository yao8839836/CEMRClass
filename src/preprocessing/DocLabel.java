package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DocLabel {

	/**
	 * 获得医案的label
	 * 
	 * @param files
	 *            原始文件列表
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Integer> getDocumentLabel(File[] files) throws IOException {

		Map<String, Integer> doc_label = new HashMap<>();

		for (File f : files) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			String file_str = f.toString();

			String filename = file_str.substring(file_str.lastIndexOf("\\") + 1, file_str.lastIndexOf("."));

			String line = reader.readLine();

			reader.close();

			if (line.startsWith("00"))
				doc_label.put(filename, 0);
			else if (line.startsWith("01"))
				doc_label.put(filename, 1);
			else if (line.startsWith("03"))
				doc_label.put(filename, 3);
			else if (line.startsWith("05"))
				doc_label.put(filename, 5);
			else if (line.startsWith("06"))
				doc_label.put(filename, 6);

		}

		return doc_label;
	}

}
