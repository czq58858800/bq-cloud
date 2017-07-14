package com.bqst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
//创建服务注册中心
@EnableEurekaServer
@SpringBootApplication
public class discoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(discoveryApplication.class, args);
	}
}
