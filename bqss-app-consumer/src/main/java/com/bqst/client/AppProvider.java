package com.bqst.client;

import com.bqst.core.Result;
import com.bqst.core.ResultCode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by chenzq on 2017/7/13 0013 11:15
 * Biao Qing Technology
 */
@Service
public class AppProvider {
    //服务id
    private String ServiceId = "http://app-provider";
    @Autowired
    RestTemplate restTemplate;


    /**
     *
     * 获取用户列表
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback")//熔断方法
    public Result getUserlist() {
        return restTemplate.getForObject(ServiceId + "/user/list", Result.class);
    }


    /**
     * 错误熔断 服务降级
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
