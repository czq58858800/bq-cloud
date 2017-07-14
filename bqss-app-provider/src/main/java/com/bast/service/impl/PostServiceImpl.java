package com.bast.service.impl;

import com.bast.dao.PostMapper;
import com.bast.model.Post;
import com.bast.service.PostService;
import com.bast.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by chenzhongqiang on 2017/07/12.
 */
@Service
@Transactional
public class PostServiceImpl extends AbstractService<Post> implements PostService {
    @Resource
    private PostMapper postMapper;

}
