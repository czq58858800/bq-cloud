package com.bast.core;


import com.bast.core.util.ExceptionUtil;
import com.bast.service.RedisService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T extends BaseModel> implements Service<T> {

    private Logger logger = LogManager.getLogger(AbstractService.class);

    @Autowired
    protected Mapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    @Autowired
    protected RedisService redisService;

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T model) {
        try {
            mapper.insertSelective(model);
        } catch (DuplicateKeyException e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException("已经存在相同的配置.");
        } catch (Exception e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException(msg);
        }

    }

    @Transactional
    public void save(List<T> models) {
        try {
            mapper.insertList(models);
        } catch (DuplicateKeyException e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException("已经存在相同的配置.");
        } catch (Exception e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException(msg);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        try {
            mapper.deleteByPrimaryKey(id);
            redisService.del(getCacheKey(id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Transactional
    public T update(T model) {
        try {
            String lockKey = getLockKey(model.getId());
            if (StringUtils.isBlank(lockKey)) {
                mapper.updateByPrimaryKeySelective(model);
            } else {
                if (redisService.getLock(lockKey)) {
                    try {
                        mapper.updateByPrimaryKeySelective(model);
                        model = findById(model.getId());
                        redisService.set(getCacheKey(model.getId()), model);
                    } finally {
                        redisService.unlock(lockKey);
                    }
                } else {
                    sleep(20);
                    return update(model);
                }
            }
        } catch (DuplicateKeyException e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException("已经存在相同的配置.");
        } catch (Exception e) {
            String msg = ExceptionUtil.getStackTraceAsString(e);
            logger.error(Constants.Exception_Head + msg, e);
            throw new RuntimeException(msg);
        }
        return model;
    }

    public T findById(Integer id) {
        try {
            String key = getCacheKey(id);
            if (StringUtils.isBlank(key)) {
                return mapper.selectByPrimaryKey(id);
            } else {
                T record = (T)redisService.get(key);
                if (null != record) {
                    logger.info("cache hit......");
                }
                if (record == null) {
                    String lockKey = getLockKey(id);
                    if (redisService.getLock(lockKey)) {
                        logger.info("cache miss read db......");
                        record = mapper.selectByPrimaryKey(id);
                        redisService.set(key, record);
                        redisService.del(lockKey);
                    } else {
                        sleep(20);
                        return findById(id);
                    }
                }
                return record;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(RandomUtils.nextLong(10, millis));
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    /** 获取缓存键值 */
    protected String getCacheKey(Object id) {
        String cacheName = getCacheKey();
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        return new StringBuilder(Constants.CACHE_NAMESPACE).append(cacheName).append(":").append(id).toString();
    }

    /** 获取缓存键值 */
    protected String getLockKey(Object id) {
        String cacheName = getCacheKey();
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        return new StringBuilder(Constants.CACHE_NAMESPACE).append(cacheName).append(":LOCK:").append(id).toString();
    }

    /**
     * @return
     */
    private String getCacheKey() {
        Class<?> cls = getClass();
        String cacheName = Constants.cacheKeyMap.get(cls);
        if (StringUtils.isBlank(cacheName)) {
            CacheConfig cacheConfig = cls.getAnnotation(CacheConfig.class);
            if (cacheConfig == null) {
                return null;
            } else if (cacheConfig.cacheNames() == null || cacheConfig.cacheNames().length < 1) {
                cacheName = getClass().getName();
            } else {
                cacheName = cacheConfig.cacheNames()[0];
            }
            Constants.cacheKeyMap.put(cls, cacheName);
        }
        return cacheName;
    }
}
