package com.weixin.cache.codec.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.weixin.cache.codec.ITranscoder;


public class LongTranscoder implements ITranscoder {
	public void encode(DataOutputStream out, Object obj) throws IOException {
		Long l = (Long) obj;
		out.writeLong(l.longValue());
	}

	public Object decode(DataInputStream in) throws IOException {
		return Long.valueOf(in.readLong());
	}
}
