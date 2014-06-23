package com.weixin.cache;

import com.weixin.cache.codec.ITranscoder;


public class MemKey {
	public static final int NULL = 0;
	public static final int SENCOND = 1;
	public static final int MINUTE = 60;
	public static final int HOUR = 3600;
	public static final int DAY = 86400;
	
	/**键名,支持运行时动态生成{@link #build(Object...)}*/
	private String key;
	/**编解码器*/
	private ITranscoder transcoder;
	/**键生存时间设置*/
	private int expiredTime;
	
	
	public MemKey(String key, int expiredTime,ITranscoder transcoder) {
		this.key = key;
		this.expiredTime = expiredTime;
		this.transcoder = transcoder;
	}
	
	public MemKey build(Object... params){
		MemKey newKey = new MemKey(key, expiredTime, transcoder);
		newKey.key = String.format(key, params);
		return newKey;
	}
	

	public String getKey() {
		return key;
	}


	public ITranscoder getTranscoder() {
		return transcoder;
	}


	public int getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(int expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	
}
