package test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import util.Corpus;
import util.ReadWriteFile;
import wordvector.Word2Vec;
import feature.DocVector;

public class VisualizeData {

	public static void main(String[] args) throws IOException {

		Map<String, Integer> doc_label = Corpus.getDocLabel("data//label.txt");

		// 方法一模一样, 就不重写了
		List<String> doc_id_list = Corpus.getVocab("data//word2vec_doc_id.txt");

		Map<String, double[]> vector_map = DocVector.getDocVector(doc_id_list, "file//clinical_doc.vec");

		int V = vector_map.get(doc_id_list.get(0)).length;

		double[][] matrix = new double[doc_id_list.size()][V];

		for (String id : doc_id_list) {

			matrix[doc_id_list.indexOf(id)] = vector_map.get(id);

		}

		Matrix embedding = new Matrix(matrix);

		System.out.println("Embedding = U S V^T");

		SingularValueDecomposition s = embedding.svd();

		Matrix U = s.getU();

		Matrix S = s.getS();

		// 按行压缩，降维，2维，可以可视化

		Matrix U_sub = U.getMatrix(0, U.getRowDimension() - 1, 0, 1);

		Matrix compress = U_sub.times(S.getMatrix(0, 1, 0, 1));

		double[][] compress_matrix = compress.getArray();

		StringBuilder sb = new StringBuilder();

		int doc_count = 0;

		for (double[] row : compress_matrix) {

			for (double e : row) {

				sb.append(e + "\t");
			}

			sb.append(doc_label.get(doc_id_list.get(doc_count)) + "\n");

			doc_count++;
		}

		ReadWriteFile.writeFile("file//docvec_2d.txt", sb.toString().replaceAll("\t\n", "\n"));

		Word2Vec w2v = new Word2Vec("file//clinical_word.vec");

		System.out.println(w2v.distance("麻黄"));

	}

}
