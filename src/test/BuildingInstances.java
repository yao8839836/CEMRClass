package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import feature.BOC;
import preprocessing.BuildInstances;
import util.Corpus;
import util.ReadWriteFile;

public class BuildingInstances {

	public static void main(String[] args) throws IOException {

		Map<String, Integer> doc_label = Corpus.getDocLabel("data//label.txt");

		Map<String, String> doc_train_test = Corpus.getDocTrainOrTest("data//train_or_test.txt");

		// 方法一模一样, 就不重写了

		Set<String> word_set = ReadWriteFile.getWordSet();

		Set<String> medicines = ReadWriteFile.getWordSet("data//disease_tcm_mesh.txt");

		System.out.println(medicines.size());

		System.out.println(word_set.size());

		Set<String> medicine_set = new HashSet<>();

		for (String word : word_set) {

			if (medicines.contains(word)) {

				medicine_set.add(word);
			}

		}

		List<String> concepts = new ArrayList<>(medicine_set);

		StringBuilder sb = new StringBuilder();

		for (String word : concepts) {

			sb.append(word + "\n");
		}

		ReadWriteFile.writeFile("file//disease_tcm_mesh.txt", sb.toString());

		File[] files = new File("data//ClinicalCases//").listFiles();

		Map<String, double[]> vector_map = BOC.binary_vector(files, concepts);

		int V = concepts.size();

		String training_data = BuildInstances.getTrainingSet(doc_label, doc_train_test, vector_map, V);

		String test_data = BuildInstances.getTestSet(doc_label, doc_train_test, vector_map, V);

		ReadWriteFile.writeFile("file//train_boc_disease_tcm_mesh.arff", training_data);

		ReadWriteFile.writeFile("file//test_boc_disease_tcm_mesh.arff", test_data);
	}
}
