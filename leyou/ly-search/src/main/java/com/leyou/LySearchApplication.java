package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description 注解：作为feign的客户端，可以被注册中心发现，springboot启动类
 * @Author TT Hun
 * @Data 2019/11/26 20:05
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class,args);
    }
}