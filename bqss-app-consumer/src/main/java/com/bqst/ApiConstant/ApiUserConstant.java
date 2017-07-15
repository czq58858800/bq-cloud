package com.bqst.ApiConstant;

/**
 * Created by chenzq on 2017/7/15 0015 17:15
 * Biao Qing Technology
 */
public interface ApiUserConstant {
    /**
     * 服务id
     */
    public String SERVICE_HOSTNAME = "http://bqss-app-provider";
    /**
     * 用户详情
     */
    public String USER_DETAIL = "/user/detail";

    static String buildUrl(String url) {
        return SERVICE_HOSTNAME + url;
    }

}
