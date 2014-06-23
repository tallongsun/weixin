package com.weixin.util;

import java.util.Arrays;
import java.io.UnsupportedEncodingException;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TTransport;

public class ThriftSerialize {
	/**
	 * 将对象序列化成二进制
	 * 
	 * @param base
	 * @return
	 */
	public static byte[] serialize(TBase<?, ?> base) {
		if (base == null) {
			return null;
		}
		
		return serialize(base, null);
	}
	
	/**
	 * 将对象序列化成二进制
	 * 
	 * @param base
	 * @param serializer
	 * 
	 * @return
	 */
	public static byte[] serialize(TBase<?, ?> base, ThriftMsgSerializer serializer) {
		if (base == null) {
			return null;
		}
		
		try {
			if(serializer == null) {
				serializer = new ThriftMsgSerializer(new TCompactProtocol.Factory());
			}
			return serializer.serialize(base);
		} catch (TException e) {
			throw new RuntimeException("thrift serialize error. base=" + base);
		}
	}
	
	/**
	 * 从二进制数据反序列化成对象
	 * 
	 * @param base
	 * @param bytes
	 */
	public static void deSerialize(TBase<?, ?> base, byte[] bytes) {
		if (base == null || bytes == null) {
			return;
		}
		
		deSerialize(base, bytes, null);
	}

	/**
	 * 从二进制数据反序列化成对象
	 * 
	 * @param base
	 * @param bytes
	 * @param deserializer
	 * 
	 */
	public static void deSerialize(TBase<?, ?> base, byte[] bytes, TDeserializer deserializer) {
		if (base == null || bytes == null) {
			return;
		}
		try {
			if(deserializer == null) {
				deserializer = new TDeserializer(new TCompactProtocol.Factory());
			}
			deserializer.deserialize(base, bytes);
		} catch (TException e) {
			throw new RuntimeException("thrift deSerialize error. base=" + base + " bytes=" + Arrays.toString(bytes), e);
		}
	}
	
}

class ThriftMsgSerializer {

	/**
	 * This transport wraps that byte array
	 */
	private final ThriftMemoryBufferTransport transport_ = new ThriftMemoryBufferTransport(64);

	/**
	 * Internal protocol used for serializing objects.
	 */
	private TProtocol protocol_;

	/**
	 * Create a new TSerializer. It will use the TProtocol specified by the
	 * factory that is passed in.
	 * 
	 * @param protocolFactory
	 *            Factory to create a protocol
	 */
	public ThriftMsgSerializer(TProtocolFactory protocolFactory) {
		protocol_ = protocolFactory.getProtocol(transport_);
	}

	/**
	 * Serialize the Thrift object into a byte array. The process is simple,
	 * just clear the byte array output, write the object into it, and grab the
	 * raw bytes.
	 * 
	 * @param base
	 *            The object to serialize
	 * @return Serialized object in byte[] format
	 */
	@SuppressWarnings("unchecked")
	public byte[] serialize(TBase base) throws TException {
		transport_.reset();
		base.write(protocol_);

		byte[] bytes = new byte[transport_.length()];
		transport_.read(bytes, 0, bytes.length);
		return bytes;
	}

	/**
	 * Serialize the Thrift object into a Java string, using a specified
	 * character set for encoding.
	 * 
	 * @param base
	 *            The object to serialize
	 * @param charset
	 *            Valid JVM charset
	 * @return Serialized object as a String
	 */
	@SuppressWarnings("unchecked")
	public String toString(TBase base, String charset) throws TException {
		try {
			return new String(serialize(base), charset);
		} catch (UnsupportedEncodingException uex) {
			throw new TException("JVM DOES NOT SUPPORT ENCODING: " + charset);
		}
	}

	/**
	 * Serialize the Thrift object into a Java string, using the default JVM
	 * charset encoding.
	 * 
	 * @param base
	 *            The object to serialize
	 * @return Serialized object as a String
	 */
	@SuppressWarnings("unchecked")
	public String toString(TBase base) throws TException {
		return new String(serialize(base));
	}
}

class ThriftMemoryBufferTransport extends TTransport {
	// The contents of the buffer
	private TByteArrayOutputStream arr_;

	// Position to read next byte from
	private int pos_;

	public ThriftMemoryBufferTransport(int size) {
		arr_ = new TByteArrayOutputStream(size);
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public void open() {
		/* Do nothing */
	}

	@Override
	public void close() {
		/* Do nothing */
	}

	public void reset() {
		arr_.reset();
		pos_ = 0;
	}

	@Override
	public int read(byte[] buf, int off, int len) {
		byte[] src = arr_.get();
		int amtToRead = (len > arr_.len() - pos_ ? arr_.len() - pos_ : len);
		if (amtToRead > 0) {
			System.arraycopy(src, pos_, buf, off, amtToRead);
			pos_ += amtToRead;
		}
		return amtToRead;
	}

	@Override
	public void write(byte[] buf, int off, int len) {
		arr_.write(buf, off, len);
	}

	/**
	 * Output the contents of the memory buffer as a String, using the supplied
	 * encoding
	 * 
	 * @param enc
	 *            the encoding to use
	 * @return the contents of the memory buffer as a String
	 */
	public String toString(String enc) throws UnsupportedEncodingException {
		return arr_.toString(enc);
	}

	public String inspect() {
		String buf = "";
		byte[] bytes = arr_.toByteArray();
		for (int i = 0; i < bytes.length; i++) {
			buf += (pos_ == i ? "==>" : "")
					+ Integer.toHexString(bytes[i] & 0xff) + " ";
		}
		return buf;
	}

	public int length() {
		return arr_.size();
	}

	public byte[] getArray() {
		return arr_.get();
	}
}
