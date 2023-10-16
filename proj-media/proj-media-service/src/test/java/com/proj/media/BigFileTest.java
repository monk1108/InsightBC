package com.proj.media;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.*;
import java.util.Collections;

/**
 * @Description: Test on processing large file
 * @Author: Yinuo
 * @Date: 2023/10/16 0:55
 */
public class BigFileTest {

    @Test
    public void testChunk() throws IOException {
        File originalFile = new File("F:\\mine\\learning\\java\\xuechengzaixian\\exp.mp4");
//        chunk file saving path
        String chunkFilePath = "F:\\mine\\learning\\java\\xuechengzaixian\\chunk\\";
        File chunkFolder = new File(chunkFilePath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }

//        chunk size (1 Mb)
        long chunkSize = 1024 * 1024 * 1;
//        chunk nums
        long chunkNum = (long) Math.ceil((double) originalFile.length() * 1.0/ chunkSize);
        System.out.println("Chunk Num: " + chunkNum);

//        Random Access File: a file stream where both reading and writing is enabled
//        buffer size
        byte[] b = new byte[1024];
        RandomAccessFile raf_read = new RandomAccessFile(originalFile, "r");
//        chunking
        for (int i = 0; i < chunkNum; i++) {
            File file = new File(chunkFilePath + i);
            if (file.exists()) {
                file.delete();
            }

            boolean newFile = file.createNewFile();
            if (newFile) {
//                write data into chunk file
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
//                length: how many bytes did you read from the original file
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                    if (file.length() >=  chunkSize) {
                        break;
                    }
                }
                raf_write.close();
                System.out.println("Chunk " + i + " created");
            }
        }

        raf_read.close();

    }

    @Test
    public void testMerge() throws IOException {
        File chunkFolder = new File("F:\\mine\\learning\\java\\xuechengzaixian\\chunk\\");
        File originalFile = new File("F:\\mine\\learning\\java\\xuechengzaixian\\exp.mp4");
        File mergeFile = new File("F:\\\\mine\\\\learning\\\\java\\\\xuechengzaixian\\nacos01.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }

        mergeFile.createNewFile();
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
//        pointer pointing to the top of the file
        raf_write.seek(0);
        byte[] b = new byte[1024];
        File[] fileArray = chunkFolder.listFiles();
        List<File> fileList = Arrays.asList(fileArray);

//        sorting chunk file folder in name order
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                    return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
                }
        });

//        merge chunks
        for (File chunkFile : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();

//        check merge file integrity
        try (
//                with this try-with-resources, fileInputStream and mergeFileStream (the resources defined in the brackets)
//                will be closed automatically after the try block ends.
                FileInputStream fileInputStream = new FileInputStream(originalFile);
                FileInputStream mergeFileStream = new FileInputStream(mergeFile);
                ) {
            String originalMd5 = DigestUtils.md5DigestAsHex(fileInputStream);
            String mergeMd5 = DigestUtils.md5DigestAsHex(mergeFileStream);
            if (originalMd5.equals(mergeMd5)) {
                System.out.println("Merge Success");
            } else {
                System.out.println("Merge Failure");
            }
        }

    }
}
