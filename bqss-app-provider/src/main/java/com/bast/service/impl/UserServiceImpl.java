package com.bast.service.impl;

import com.bast.core.AbstractService;
import com.bast.core.Constants;
import com.bast.dao.UserMapper;
import com.bast.model.User;
import com.bast.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by chenzhongqiang on 2017/07/13.
 */
@Service
@Transactional
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"User")
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
