package com.weixin.msg.res;

import java.util.List;

import com.weixin.msg.BaseResMessage;

public class NewsResMessage extends BaseResMessage{
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;
	
	
	public NewsResMessage(int articleCount,List<Article> articles) {
		super();
		this.setMsgType("news");
		this.ArticleCount = articleCount;
		this.Articles = articles;
	}
	
	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

	public static class Article {
		// 图文消息名称
		private String Title;
		// 图文消息描述
		private String Description;
		// 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
		private String PicUrl;
		// 点击图文消息跳转链接
		private String Url;

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public String getDescription() {
			return null == Description ? "" : Description;
		}

		public void setDescription(String description) {
			Description = description;
		}

		public String getPicUrl() {
			return null == PicUrl ? "" : PicUrl;
		}

		public void setPicUrl(String picUrl) {
			PicUrl = picUrl;
		}

		public String getUrl() {
			return null == Url ? "" : Url;
		}

		public void setUrl(String url) {
			Url = url;
		}

		@Override
		public String toString() {
			return "Article [Description=" + Description + ", PicUrl=" + PicUrl
					+ ", Title=" + Title + ", Url=" + Url + "]";
		}
		
		
	}
	
}
