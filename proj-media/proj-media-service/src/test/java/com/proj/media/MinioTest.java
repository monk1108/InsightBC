package com.proj.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    .filename("F:\\mine\\learning\\java\\projzaixian\\学成在线项目—资料\\day05 媒资管理 Nacos Gateway MinIO\\myvideo.mp4")
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

    /***
     * @description Upload Chunk File to minio
            *
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/19 13:08:14
            */
    @Test
    public void uploadChunk() {
        String chunkFolderPath = "F:\\mine\\learning\\java\\xuechengzaixian\\chunk\\";
        File chunkFolder = new File(chunkFolderPath);
//        chunk files
        File[] chunkFiles = chunkFolder.listFiles();

//        upload chunk files to minio
        for (int i = 0; i < chunkFiles.length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                        .bucket("testbucket")
                        .object("chunk/" + i)
                        .filename(chunkFiles[i].getAbsolutePath())
                        .build();
                minioClient.uploadObject(uploadObjectArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description Merge chunk files in minio
            *
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/19 13:15:54
            */
    @Test
    public void testMerge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(3)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

//        set object name after merge
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("testbucket")
                .object("restored.mp4")
                .sources(sources)
                .build();

        minioClient.composeObject(composeObjectArgs);
    }

    /**
     * @description Delete chunk files in minio
            *
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/19 13:32:58
            */
    @Test
    public void testDeleteObjects() {
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(3)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectArgs = RemoveObjectsArgs.builder()
                .bucket("testbucket")
                .objects(deleteObjects)
                .build();

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectArgs);
        results.forEach(r -> {
            try {
                DeleteError error = r.get();
                System.out.println(error.objectName() + " " + error.message());
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

    }
}

