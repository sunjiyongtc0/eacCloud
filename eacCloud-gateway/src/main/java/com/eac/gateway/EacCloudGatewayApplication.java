package com.eac.gateway;

import com.eac.gateway.filter.GatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 * Title : 网关启动类
 * </pre>
 *
 **/
@SpringBootApplication(scanBasePackages = "com.eac")
@EnableDiscoveryClient
public class EacCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EacCloudGatewayApplication.class, args);
    }

    @Bean
    public GatewayFilter gatewayFilter() {
        return new GatewayFilter();
    }
}
