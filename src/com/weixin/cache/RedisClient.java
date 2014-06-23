package com.weixin.cache;

import java.util.HashMap;
import java.util.Map;

import com.weixin.cache.codec.IMapTranscoder;
import com.weixin.cache.codec.ITranscoder;
import com.weixin.cache.codec.TranscoderManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {
	/** 连接池管理连接，具体配置 @see {@link #RedisConfig} */
	private JedisPool jedisPool;

	public RedisClient(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	/** -----------------字符串键值操作 start-----------------*/
	@SuppressWarnings("unchecked")
	public <T> T get(MemKey key){
		Jedis jedis = this.jedisPool.getResource();
		try{
			if(key.getTranscoder() == null){
				return (T)jedis.get(key.getKey());
			}
			byte[] bytes = jedis.get(key.getKey().getBytes());
			if(bytes == null || bytes.length < 1){
				return null;
			}
			return (T)TranscoderManager.decode(bytes, key.getTranscoder());
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	
	public boolean set(MemKey key, Object value){
		Jedis jedis = this.jedisPool.getResource();
		try{
			if(value == null){
				del(key);
				return true;
			}
			
			byte[] bytes;
			if(key.getTranscoder()==null){
				bytes = value.toString().getBytes();
			}else{
				bytes = TranscoderManager.encode(value,key.getTranscoder());
			}
			if(bytes.length<1){
				return false;
			}
			//键生存时间设置
			if(key.getExpiredTime()>0){
				return jedis.setex(key.getKey().getBytes(), key.getExpiredTime(), bytes) != null;
			}else{
				return jedis.set(key.getKey().getBytes(), bytes)!=null;
			}
			
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	
	public void del(MemKey key){
		Jedis jedis = this.jedisPool.getResource();
		try{
			jedis.del(key.getKey().getBytes());
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	/** -----------------字符串键值操作 end-----------------*/
	
	/** -----------------散列键值操作 start-----------------*/
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> hgetAll(MemKey key){
		Jedis jedis = this.jedisPool.getResource();
		try{
			Map<byte[],byte[]> map = jedis.hgetAll(key.getKey().getBytes());
			if(map == null || map.size() == 0){
				return new HashMap<K, V>(1);
			}
			Map<K,V> newMap = new HashMap<K, V>(map.size());
			for(Map.Entry<byte[],byte[]> entry:map.entrySet()){
				K field = (K)decodeField(entry.getKey(), key.getTranscoder());
				V value;
				if(key.getTranscoder() == null){
					value = (V)new String(entry.getValue());
				}else{
					value = (V)TranscoderManager.decode(entry.getValue(),key.getTranscoder());
				}
				newMap.put(field, value);
			}
			return newMap;
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	
	public long hset(MemKey key, Object field, Object value){
		Jedis jedis = this.jedisPool.getResource();
		try{
			byte[] keyBytes = key.getKey().getBytes();
			byte[] bytes;
			if(key.getTranscoder()==null){
				bytes = value.toString().getBytes();
			}else{
				bytes = TranscoderManager.encode(value,key.getTranscoder());
			}
			long result = jedis.hset(keyBytes, encodeField(field, key.getTranscoder()), bytes);
			//键生存时间设置
			if(key.getExpiredTime()>0){
				jedis.expire(keyBytes, key.getExpiredTime());
			}
			return result;
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	
	public long hdel(MemKey key, Object[] fields){
		if ((fields == null) || (fields.length == 0)) {
			return 0L;
		}
		Jedis jedis = this.jedisPool.getResource();
		try{
			byte[][] bytes = new byte[fields.length][];
			for (int i = 0; i < fields.length; i++) {
				bytes[i] = encodeField(fields[i], key.getTranscoder());
			}
			long result = jedis.hdel(key.getKey().getBytes(),bytes);
			//键生存时间设置
			if(key.getExpiredTime()>0){
				jedis.expire(key.getKey().getBytes(), key.getExpiredTime());
			}
			return result;
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	
	public long hlen(MemKey key){
		Jedis jedis = this.jedisPool.getResource();
		try{
			return jedis.hlen(key.getKey().getBytes());
		}finally{
			this.jedisPool.returnResource(jedis);
		}
	}
	/** -----------------散列键值操作 end-----------------*/
	
	
	private byte[] encodeField(Object field, ITranscoder transcoder) {
		if ((transcoder instanceof IMapTranscoder)) {
			ITranscoder fieldTranscoder = ((IMapTranscoder)transcoder).getFieldTranscoder();
			if (fieldTranscoder != null) {
				return TranscoderManager.encode(field, fieldTranscoder);
			}
		}
		return field.toString().getBytes(); 
	}
	
	private Object decodeField(byte[] bytes, ITranscoder transcoder){
		if ((transcoder instanceof IMapTranscoder)) {
			ITranscoder fieldTranscoder = ((IMapTranscoder)transcoder).getFieldTranscoder();
			if (fieldTranscoder != null) {
				return TranscoderManager.decode(bytes, fieldTranscoder);
			}
		}
		 return new String(bytes);
	}
	
}
