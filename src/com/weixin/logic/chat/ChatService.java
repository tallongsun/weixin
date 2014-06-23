package com.weixin.logic.chat;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.weixin.db.dao.ChatDao;

public class ChatService {
	public static final Logger log = LoggerFactory.getLogger(ChatLogic.class);
	
	/** 索引文件的目录路径 */
	private static String indexDirPath = "";
	
	public static void init(String rootPath){
		indexDirPath = rootPath + "/index/";
		File indexDir = new File(indexDirPath);
		if(indexDir.exists()){
			return;
		}
		createIndex(indexDir);
	}	
	
	public static String chat(String req,String openId){
		log.debug("question:"+req);
		
		String answer = null;
		int chatCategory = 0;
		Knowledge knowledge = searchIndex(req);
		// 找到匹配项
		if (null != knowledge) {
			if (2 == knowledge.getCategory()) {// 笑话
				answer = ChatDao.getJoke();
				chatCategory = 2;
			} else if (3 == knowledge.getCategory()) {// 上下文
				// 判断上一次的聊天类别
				int category = ChatDao.getLastCategory(openId);
				if (2 == category) {// 如果是笑话，本次继续回复笑话给用户
					answer = ChatDao.getJoke();
					chatCategory = 2;
				} else {
					answer = knowledge.getAnswer();
					chatCategory = knowledge.getCategory();
				}
			} else {// 普通对话
				answer = knowledge.getAnswer();
				// 如果答案为空，根据知识id从问答知识分表中随机获取一条
				if ("".equals(answer))
					answer = ChatDao.getKnowledSub(knowledge.getId());
				chatCategory = 1;
			}
		} else {// 未找到匹配项
			answer = getDefaultAnswer();
			chatCategory = 0;
		}
		// 保存聊天记录
		ChatDao.saveChatLog(openId, new Date().getTime()+"", req, answer, chatCategory);
		log.debug("answer:"+answer);
		
		return answer;
	}
	
	private static String getDefaultAnswer() {
		String []answer = {
			"要不我们聊点别的？",
			"恩？你到底在说什么呢？",
			"没有听懂你说的，能否换个说法？",
			"虽然不明白你的意思，但我却能用心去感受",
			"听的我一头雾水，阁下的知识真是渊博呀，膜拜~",
			"真心听不懂你在说什么，要不你换种表达方式如何？",
			"哎，我小学语文是体育老师教的，理解起来有点困难哦",
			"是世界变化太快，还是我不够有才？为何你说话我不明白？"
		};
		return answer[new Random().nextInt(answer.length)];
	}
	
	private static void createIndex(File indexDir) {
		// 取得问答知识库中的所有记录
		List<Knowledge> knowledgeList = ChatDao.findAllKnowledge();
		Directory directory = null;
		IndexWriter indexWriter = null;
		try {
			directory = FSDirectory.open(indexDir);
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_46, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, iwConfig);
			Document doc = null;
			// 遍历问答知识库创建索引
			for (Knowledge knowledge : knowledgeList) {
				doc = new Document();
				// 对question进行分词存储
				doc.add(new TextField("question", knowledge.getQuestion(), Store.YES));
				// 对id、answer和category不分词存储
				doc.add(new IntField("id", knowledge.getId(), Store.YES));
				doc.add(new StringField("answer", knowledge.getAnswer(), Store.YES));
				doc.add(new IntField("category", knowledge.getCategory(), Store.YES));
				indexWriter.addDocument(doc);
			}
			indexWriter.close();
			directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static Knowledge searchIndex(String content) {
		Knowledge knowledge = null;
		try {
			Directory directory = FSDirectory.open(new File(indexDirPath));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			// 使用查询解析器创建Query
			QueryParser questParser = new QueryParser(Version.LUCENE_46, "question", new IKAnalyzer(true));
			Query query = questParser.parse(QueryParser.escape(content));
			// 检索得分最高的文档
			TopDocs topDocs = searcher.search(query, 1);
			if (topDocs.totalHits > 0) {
				knowledge = new Knowledge();
				ScoreDoc[] scoreDoc = topDocs.scoreDocs;
				for (ScoreDoc sd : scoreDoc) {
					Document doc = searcher.doc(sd.doc);
					knowledge.setId(doc.getField("id").numericValue().intValue());
					knowledge.setQuestion(doc.get("question"));
					knowledge.setAnswer(doc.get("answer"));
					knowledge.setCategory(doc.getField("category").numericValue().intValue());
				}
			}
			reader.close();
			directory.close();
		} catch (Exception e) {
			knowledge = null;
			e.printStackTrace();
		}
		return knowledge;
	}
}
