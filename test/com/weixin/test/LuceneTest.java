package com.weixin.test;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneTest {
	// 索引存储位置
	private String indexDir = "E:/indexDir";
	// Field名称
	private String fieldName = "content";

	/**
	 * 创建索引
	 * 
	 * @param analyzer 分词器
	 * @throws IOException
	 */
	public void createIndex(Analyzer analyzer) throws IOException {
		File file = new File(indexDir);
		if(file.exists()){
			return;
		}
		
		// 待索引的文本数据
		String[] contentArr = { 
			"考进清华北大是许多人的梦想", 
			"清华是中国著名高等学府", 
			"清华大学是世界上最美丽的大学之一"
		};
		// 创建或打开索引目录
		Directory directory = FSDirectory.open(file);
		// 创建IndexWriter
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		// 遍历数组创建索引
		for (String text : contentArr) {
			// 创建document并添加field
			Document document = new Document();
			document.add(new TextField(fieldName, text, Field.Store.YES));
			// 将document添加到索引中
			indexWriter.addDocument(document);
		}
		indexWriter.commit();
		indexWriter.close();
		directory.close();
	}

	/**
	 * 从索引中检索
	 * 
	 * @param sentence 检索语句
	 * @param analyzer 分词器
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void searchIndex(String sentence, Analyzer analyzer) throws Exception {
		// 创建或打开索引目录
		Directory directory = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		// 使用查询解析器创建Query
		QueryParser parser = new QueryParser(Version.LUCENE_46, fieldName, analyzer);
		Query query = parser.parse(sentence);
		// 输出解析后的查询语句
		System.out.println("查询语句：" + query.toString());
		// 从索引中搜索得分排名前10的文档
		TopDocs topDocs = searcher.search(query, 10);
		ScoreDoc[] scoreDoc = topDocs.scoreDocs;
		System.out.println("共检索到" + topDocs.totalHits + "条匹配结果");
		for (ScoreDoc sd : scoreDoc) {
			// 根据id获取document
			Document d = searcher.doc(sd.doc);
			System.out.println(d.get(fieldName) + " score:" + sd.score);
			// 查看文档得分解析
			System.out.println(searcher.explain(query, sd.doc));
		}
		reader.close();
		directory.close();
	}

	public static void main(String[] args) throws Exception {
		// 测试更细粒度分词
		testAnalyzer();
		
		// 创建分词器,使用智能划分
		Analyzer analyzer = new IKAnalyzer(true);

		LuceneTest luceneTest = new LuceneTest();
		// 创建索引
		luceneTest.createIndex(analyzer);
		// 从搜索中检索
		luceneTest.searchIndex("梦想上清华", analyzer);
	}
	
	
	public static void testAnalyzer() throws Exception {
		String content = "IK Analyzer分词测试：生科院的研究生研究生命科学。";
		// true:智能切分，false:细粒度切分
		Analyzer analyzer = new IKAnalyzer(false);
		// 对content进行分词，得到的结果是分词流
		TokenStream ts = analyzer.tokenStream("text", content);
		ts.reset();

		CharTermAttribute attr = null;
		// 遍历分词流
		while (ts.incrementToken()) {
			attr = ts.getAttribute(CharTermAttribute.class);
			System.out.print(attr.toString() + " ");
		}
		System.out.println();
	}
}