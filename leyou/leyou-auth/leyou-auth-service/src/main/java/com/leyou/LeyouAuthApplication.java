package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/4/4 19:57
 */
@SpringBootApplication
@EnableDiscoveryClient
@FeignClient
public class LeyouAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouAuthApplication.class);
    }
}
