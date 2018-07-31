package com.maurofokker.poc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class SpringMicroservicesZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMicroservicesZuulApplication.class, args);
	}
}
