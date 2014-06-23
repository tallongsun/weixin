package com.weixin.cache.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.weixin.util.QuickLZ;


/**
 *使用transcoder编解码 
 *    如果数据大小超过{@link #compressShold}, 自动使用{@link #QuickLZ}算法压缩
 */
public class TranscoderManager {

	private static int compressShold = 100;
	
	public static byte[] encode(Object obj, ITranscoder transcoder) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bos);
	
		try{
			transcoder.encode(out, obj);
			
			byte[] bytes = bos.toByteArray();
			byte[] newBytes;
			if(bytes.length<compressShold){
				newBytes = new byte[bytes.length+1];
				newBytes[0] = 0x1F;
				System.arraycopy(bytes, 0, newBytes, 1, bytes.length);
			}else{
				byte[] compressBytes = QuickLZ.compress(bytes, 1);
				newBytes = new byte[compressBytes.length+1];
				newBytes[0] = 0x3F;
				System.arraycopy(compressBytes, 0, newBytes, 1, compressBytes.length);
			}
			
			return newBytes;
		}catch (IOException e) {
			throw new TranscoderException(e);
		}
	}
	
	public static Object decode(byte[] bytes, ITranscoder transcoder){
		if ((bytes == null) || (bytes.length < 1)) {
			return null;
		}
		byte b = bytes[0];
		if((b&0x1F)!=0x1F){
			throw new TranscoderException("The bytes is not use transcoder encoding.");
		}
		
		byte[] compressBytes = null;
		if((b&0x20)==0x20){
			compressBytes = new byte[bytes.length-1];
			System.arraycopy(bytes, 1, compressBytes, 0, compressBytes.length);
			bytes = QuickLZ.decompress(compressBytes);
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(bis);
		try{
			if(compressBytes==null){
				in.readByte();
			}
			return transcoder.decode(in);
		}catch (IOException e) {
			throw new TranscoderException(e);
		}
	}
}
