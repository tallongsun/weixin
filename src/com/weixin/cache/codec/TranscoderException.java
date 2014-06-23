package com.weixin.cache.codec;

public class TranscoderException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TranscoderException(String msg){
		super(msg);
	}
	
	public TranscoderException(Exception e) {
		super(e);
	}
	
	public TranscoderException(String msg, Exception e) {
		super(msg,e);
	}
}