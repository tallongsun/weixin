package com.weixin.cache.codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.thrift.TBase;

/**
 * 编解码器：键值编解码
 *     支持Thrift编解码{@link transcoder.util.ThriftSerialize#serialize(TBase)}
 *                     {@link transcoder.util.ThriftSerialize#deSerialize(TBase, byte[])}
 */
public interface ITranscoder {
	  public abstract void encode(DataOutputStream out, Object value)
			    throws IOException;
	
	  public abstract Object decode(DataInputStream in)
			    throws IOException;

}
