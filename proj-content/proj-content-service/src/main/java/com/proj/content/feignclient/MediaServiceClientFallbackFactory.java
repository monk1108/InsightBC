package com.proj.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/28 17:26
 */
@Component
@Slf4j
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient(){
            @Override
            public String uploadFile(MultipartFile upload, String objectName) {
                //降级方法
                log.debug("When calling the media service to upload files, a circuit breaker occurred and abnormal information occurred.:{}",throwable.toString(),throwable);
                return null;
            }
        };
    }
}
