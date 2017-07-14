package com.bast.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bast.service.RedisService;
import com.google.common.collect.Sets;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Serializable,Serializable> redisTemplate;

    private Logger logger = LogManager.getLogger(RedisServiceImpl.class);

    private final Integer EXPIRE = 600;

    @Override
    public void set(final String key, final Serializable value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    @Override
    public void set(final String key, final Serializable value, final int seconds) {
        set(key,value);
        expire(key, seconds);
    }

    @Override
    public boolean setnx(final String key, final Serializable value) {
        return redisTemplate.boundValueOps(key).setIfAbsent(value);
    }

    @Override
    public void hset(String key, String field, Serializable value) {
        redisTemplate.boundHashOps(key).put(field, value);
    }


    @Override
    public Object get(final String key) {
        expire(key, EXPIRE);
        return redisTemplate.boundValueOps(key).get();
    }

    @Override
    public Object hget(final String key, final String field) {
        return redisTemplate.boundHashOps(key).get(field);
    }

    @Override
    public Set<Object> getAll(final String pattern) {
        Set<Object> values = Sets.newHashSet();
        Set<Serializable> keys = redisTemplate.keys(pattern);
        for (Serializable key : keys) {
            expire(key.toString(), EXPIRE);
            values.add(redisTemplate.opsForValue().get(key));
        }
        return values;
    }

    @Override
    public void del(final String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void hdel(final String key, final String field) {
        redisTemplate.boundHashOps(key).delete(field);
    }

    @Override
    public void delAll(final String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    @Override
    public String type(final String key) {
        expire(key, EXPIRE);
        return redisTemplate.type(key).getClass().getName();
    }

    @Override
    public boolean expire(final String key, final long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean expireAt(final String key, final long unixTime) {
        return redisTemplate.expireAt(key, new Date(unixTime));
    }

    @Override
    public Long ttl(final String key) {
        return null;
    }

    @Override
    public String getrange(final String key, final long startOffset, final long endOffset) {
        return null;
    }

    @Override
    public long lpush(final String key, final Object obj) {
        final String value = JSONObject.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public long rpush(final String key, final Object obj) {
        final String value = JSONObject.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public String lpop(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res =  connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }

    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean hexists(final String key, final String field) {
        if (exists(key)) {
            return redisTemplate.boundHashOps(key).hasKey(field);
        }
        return false;
    }


    @Override
    public boolean getLock(final String key) {
        try {
            if (!exists(key)) {
                synchronized (this.getClass()) {
                    if (!exists(key)) {
                        if (setnx(key, String.valueOf(System.currentTimeMillis()))) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getLock", e);
        }
        int expires = 1000 * 60 * 3;
        String currentValue = (String)get(key);
        if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis() - expires) {
            unlock(key);
            return getLock(key);
        }
        return false;
    }

    @Override
    public void unlock(final String key) {
        del(key);
    }
}
