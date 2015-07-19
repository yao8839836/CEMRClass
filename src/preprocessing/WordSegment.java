package preprocessing;

import org.fnlp.ml.types.Dictionary;
import org.fnlp.nlp.cn.tag.CWSTagger;

public class WordSegment {

	CWSTagger tag;

	/**
	 * 分词类的构造方法，调用FudanNLP，初始化模型、词表
	 * 
	 * @throws Exception
	 */
	public WordSegment() throws Exception {

		tag = new CWSTagger("models/seg.m", new Dictionary("models/vocabulary.txt"));

	}

	/**
	 * 对文本分词
	 * 
	 * @param text
	 * @return
	 */
	public String segment(String text) {

		return tag.tag(text);
	}

	/**
	 * 对文件内容分词
	 * 
	 * @param file
	 * @return
	 */
	public String segmentFile(String file) {

		return tag.tagFile(file);
	}

}
