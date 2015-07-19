package test;

import java.io.File;
import java.io.IOException;

import wordvector.Learn;
import wordvector.Word2Vec;

public class Word2VecTest {

	public static void main(String[] args) throws IOException {

		File result = new File("file//clinicalcases.txt");

		Learn lean = new Learn();

		lean.learnFile(result);

		lean.saveModel(new File("models//clinical.mod"));

		Word2Vec w2v = new Word2Vec();

		w2v.loadJavaModel("models//clinical.mod");

		double[] vector = w2v.getWordVector("麻黄");

		for (double d : vector) {

			System.out.println(d);
		}
		// 相似词

		System.out.println(w2v.distance("麻黄"));

		// 类比，甘草对于咳嗽相当于麻黄对于什么

		System.out.println(w2v.analogy("甘草", "咳嗽", "麻黄"));

	}

}
