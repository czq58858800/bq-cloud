package com.bqst;

import com.bqst.core.GatewayFilter;
import com.netflix.zuul.ZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApplication {

	/**
	 * 这里的方法返回值，不能写成com.netflix.zuul.IZuulFilter
	 * 可以写成com.netflix.zuul.ZuulFilter，或者com.jadyer.demo.GatewayFilter
	 * 虽然语法上允许返回IZuulFilter，但实际测试发现返回IZuulFilter时，网关功能却没有生效
	 */
	@Bean
	public ZuulFilter gatewayFilter() {
		return new GatewayFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}
