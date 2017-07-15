package com.bqst.client;

import com.bqst.ApiConstant.ApiUserConstant;
import com.bqst.core.Result;
import com.bqst.core.ResultCode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzq on 2017/7/13 0013 11:15
 * Biao Qing Technology
 */
@Service
public class UserService {

    private Logger logger = LogManager.getLogger(UserService.class);

    //服务id
    private String ServiceId = "http://bqss-app-provider";
    @Autowired
    RestTemplate restTemplate;


    /**
     * 获取用户列表
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback")//熔断方法
    public Result getUserlist() {
        return restTemplate.getForObject(ServiceId + "/user/list", Result.class);
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback")//熔断方法
    public Result userdetil(Integer id,String name) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(ApiUserConstant.buildUrl(ApiUserConstant.USER_DETAIL))
                .queryParam("id", id).queryParam("name", name)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, Result.class);
    }


    /**
     * 错误熔断 服务降级
     *
     * @return
     */
    public Result fallback() {
        Result result = new Result();
        result.setCode(ResultCode.FAIL);
        result.setMessage("服务" + ServiceId + "未发现");
        result.setData(null);
        return result;
    }

}
