package com.eac.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * OAuth2安全认证授权启动类
 *
 */

@SpringBootApplication(scanBasePackages = "com.eac")
@EnableDiscoveryClient
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class EacCloudAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EacCloudAuthApplication.class);
	}

}
