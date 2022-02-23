package com.eac.bigdata;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * <pre>
 * Title :  大数据启动类
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
public class EacCloudBigdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(EacCloudBigdataApplication.class);
    }

}
