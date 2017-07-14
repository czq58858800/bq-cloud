package com.bqst;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableSwagger2
public class AppConsumerApplication {

	private static final Logger LOGGER = Logger.getLogger(AppConsumerApplication.class);

	//开启软均衡负载
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		LOGGER.info("start execute BqssAppConsumerRibbonHystrixApplication...\n");
		new SpringApplicationBuilder(AppConsumerApplication.class).web(true).run(args);
		LOGGER.info("end execute BqssAppConsumerRibbonHystrixApplication...\n");
	}
}
