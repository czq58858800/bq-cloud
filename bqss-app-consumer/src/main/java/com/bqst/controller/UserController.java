package com.bqst.controller;

import com.bqst.core.Result;
import com.bqst.client.AppProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户接口",
        consumes = "application/json",
        produces = "application/json",
        protocols = "http",description = "用户接口")
public class UserController {

    @Autowired
    AppProvider userService;

    @ApiOperation(value = "获取用户")
    @RequestMapping(value = "/user/list",method = RequestMethod.GET)
    public Result getUserList() {
        return userService.getUserlist();
    }

}