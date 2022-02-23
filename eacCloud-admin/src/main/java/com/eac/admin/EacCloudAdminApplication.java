package com.eac.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * <pre>
 * Title :  系统管理启动类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@SpringBootApplication(scanBasePackages = "com.eac")
@EnableDiscoveryClient
@EnableWebSecurity
@EnableFeignClients(basePackages = { "com.eac" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EacCloudAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EacCloudAdminApplication.class);
    }

}
