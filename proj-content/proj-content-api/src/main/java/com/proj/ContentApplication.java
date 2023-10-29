package com.proj;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: Content management service startup class
 * @Author: Yinuo
 * @Date: 2023/10/5 19:37
 */
@SpringBootApplication
@EnableSwagger2Doc
@EnableFeignClients(basePackages={"com.proj.content.feignclient"})
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}