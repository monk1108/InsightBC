package com.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: Content management service startup class
 * @Author: Yinuo
 * @Date: 2023/10/5 19:37
 */
@SpringBootApplication
//@EnableSwagger2Doc
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}