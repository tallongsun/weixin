package com.weixin.cache.codec.custom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.thrift.TBase;

import com.weixin.cache.codec.IMapTranscoder;
import com.weixin.cache.codec.ITranscoder;
import com.weixin.cache.codec.base.LongTranscoder;
import com.weixin.util.ThriftSerialize;

/**
 * 
 * 自定义编解码器，以thrift为例 
 *
 */
public class CustomTranscoder implements IMapTranscoder {

	@SuppressWarnings("unchecked")
	public void encode(DataOutputStream out,Object value) throws IOException {
		byte[] bytes = ThriftSerialize.serialize((TBase)value);//TODO::sub class
		out.writeShort(bytes.length);
		out.write(bytes);

	}

	@SuppressWarnings("unchecked")
	public Object decode(DataInputStream in) throws IOException {
		int len = in.readUnsignedShort();
		if(len == 0){
			return null;
		}
		byte[] bytes = new byte[len];
		in.read(bytes, 0, bytes.length);
		TBase value = null;//TODO::new Object
		ThriftSerialize.deSerialize(value, bytes);
		return value;
	}

	public ITranscoder getFieldTranscoder() {
		return new LongTranscoder();
	}

}
