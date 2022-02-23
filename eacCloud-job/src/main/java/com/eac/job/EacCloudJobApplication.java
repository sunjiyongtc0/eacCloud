package com.eac.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-22
 **/
//@EnableScheduling //定时器
@SpringBootApplication(scanBasePackages = "com.eac")
@EnableDiscoveryClient
public class EacCloudJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(EacCloudJobApplication.class);
    }
}
