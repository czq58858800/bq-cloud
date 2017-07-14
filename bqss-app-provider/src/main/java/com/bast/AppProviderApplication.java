package com.bast;

import com.bast.configurer.DruidDBConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 通过 @EnableEurekaClient 注解，为服务提供方赋予注册和发现服务的能力
 * ------------------------------------------------------------------------------------------------------------------
 * 也可以使用org.springframework.cloud.client.discovery.@EnableDiscoveryClient注解
 * 详见以下两篇文章的介绍
 * ------------------------------------------------------------------------------------------------------------------
 * Created by chenzhongqiang on 2017/1/9 16:00.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties({DruidDBConfig.class})
@EnableSwagger2
public class AppProviderApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(
				AppProviderApplication.class)
				.web(true).run(args);
	}
}
