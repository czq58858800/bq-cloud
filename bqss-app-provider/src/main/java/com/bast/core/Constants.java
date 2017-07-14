package com.bast.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 常量表
 *
 * @author Harvey.Wei
 * @version $Id: Constants.java, v 0.1 2017-2-28 上午11:18:28 Harvey.Wei Exp $
 */
public interface Constants {

    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";

    /** 缓存键值 */
    public static final Map<Class<?>, String> cacheKeyMap = Maps.newHashMap();

    /** 缓存命名空间 */
    public static final String CACHE_NAMESPACE = "BiaoQing:";
    /** 缓存命名空间 */
    public static final String CACHE_SHUO_NAMESPACE = "SHUO:";

    /** 缓存命名空间 */
    public static final String FULL_CACHE_SHUO_NAMESPACE = CACHE_NAMESPACE+CACHE_SHUO_NAMESPACE;
}
