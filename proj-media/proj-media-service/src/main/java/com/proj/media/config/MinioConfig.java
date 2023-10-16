package com.proj.media.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/15 15:56
 */
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint)
                .credentials(accessKey, secretKey).build();
        return minioClient;
    }
}
