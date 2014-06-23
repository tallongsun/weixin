package com.weixin.cache.codec;



/**
 * 散列编解码器
 *     支持散列键值的域编解码
 *
 */
public interface IMapTranscoder extends ITranscoder
{
  public ITranscoder getFieldTranscoder();


}