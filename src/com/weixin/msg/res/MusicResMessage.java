package com.weixin.msg.res;

import com.weixin.msg.BaseResMessage;

public class MusicResMessage extends BaseResMessage{

	private Music Music;
	
	public MusicResMessage(Music music) {
		super();
		this.setMsgType("music");
		this.Music = music;
	}

	public Music getMusic() {
		return Music;
	}
	
	public void setMusic(Music music) {
		this.Music = music;
	}


	public static class Music {
		// 音乐名称  
	    private String Title;  
	    // 音乐描述  
	    private String Description;  
	    // 音乐链接  
	    private String MusicUrl;  
	    // 高质量音乐链接，WIFI环境优先使用该链接播放音乐  
	    private String HQMusicUrl;  
	    // 缩略图的媒体id
	    private String ThumbMediaId;
	    
		public String getTitle() {
			return Title;
		}
		public void setTitle(String title) {
			Title = title;
		}
		public String getDescription() {
			return Description;
		}
		public void setDescription(String description) {
			Description = description;
		}
		public String getMusicUrl() {
			return MusicUrl;
		}
		public void setMusicUrl(String musicUrl) {
			MusicUrl = musicUrl;
		}
		public String getHQMusicUrl() {
			return HQMusicUrl;
		}
		public void setHQMusicUrl(String hQMusicUrl) {
			HQMusicUrl = hQMusicUrl;
		}
		public String getThumbMediaId() {
			return ThumbMediaId;
		}
		public void setThumbMediaId(String thumbMediaId) {
			ThumbMediaId = thumbMediaId;
		}
		@Override
		public String toString() {
			return "Music [Description=" + Description + ", HQMusicUrl="
					+ HQMusicUrl + ", MusicUrl=" + MusicUrl + ", ThumbMediaId="
					+ ThumbMediaId + ", Title=" + Title + "]";
		}
		
		
	}
    
}

