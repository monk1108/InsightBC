package com.proj.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * @Description: Minio Test Class
 * @Author: Yinuo
 * @Date: 2023/10/15 1:54
 */
public class MinioTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    // Upload file
    @Test
    public void upload() {
        // get mimeType from name extensions
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Generic mimeType, byte stream
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
//                    .object("test001.mp4")
                    .object("001/test001.mp4") // Add subdirectory
                    .filename("F:\\mine\\learning\\java\\xuechengzaixian\\学成在线项目—资料\\day05 媒资管理 Nacos Gateway MinIO\\myvideo.mp4")
                    .contentType(mimeType) // By default, the file content type is determined based on the extension, which can also be specified.
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("Successfully upload");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to upload");
        }
    }

    @Test
    public void delete() {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket("testbucket").object("001/test001.mp4").build());
            System.out.println("Successfully delete");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to delete");
        }

    }

    @Test
    public void getFile() {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("001/test001.mp4").build();
        try(
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("F:\\1_2.mp4"));
        ) {
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

