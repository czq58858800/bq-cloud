package com.bast.service;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public interface RedisService {

    void set(final String key, final Serializable value);

    void set(final String key, final Serializable value,final int seconds);

    boolean setnx(final String key, final Serializable value);

    void hset(final String key, final String field, final Serializable value);

    Object get(final String key);

    Object hget(final String key, final String field);

    Set<Object> getAll(final String pattern);

    void del(final String key);

    void hdel(final String key, final String field);

    void delAll(final String pattern);

    String type(final String key);

    boolean expire(final String key,final long expire);

    boolean expireAt(final String key, final long unixTime);

    Long ttl(final String key);

    String getrange(final String key, final long startOffset, final long endOffset);

    long lpush(final String key,final Object obj);

    long rpush(final String key,final Object obj);

    String lpop(final String key);

    boolean exists(final String key);

    boolean hexists(final String key,final String field);

    boolean getLock(final String key);

    void unlock(String key);
}
