package com.proj.content;

import com.proj.content.config.MultipartSupportConfig;
import com.proj.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/28 16:51
 */
@SpringBootTest
public class FeignUploadTest {
    @Autowired
    MediaServiceClient mediaServiceClient;

    @Test
    public void test() {
//        convert file to multipart file
        File file = new File("F:\\test.html");

        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        mediaServiceClient.uploadFile(multipartFile, "course/2.html");
    }
}
