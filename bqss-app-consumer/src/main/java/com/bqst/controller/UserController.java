package com.bqst.controller;

import com.bqst.ApiConstant.ApiUserConstant;
import com.bqst.core.Result;
import com.bqst.client.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "用户接口",
        consumes = "application/json",
        produces = "application/json",
        protocols = "http",description = "用户接口")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "获取用户列表")
    @RequestMapping(value = "/user/list",method = RequestMethod.GET)
    public Result getUserList() {
        return userService.getUserlist();
    }


    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = ApiUserConstant.USER_DETAIL)
    public Result userDetil(HttpServletRequest request,
                            @ApiParam(required = true, value = "用户id")
                            @RequestParam(value = "id") Integer id,
                            @ApiParam(required = true, value = "用户名称")
                                @RequestParam(value = "name") String  name) {
        System.out.println(name);
        return userService.userdetil(id,name);
    }


}