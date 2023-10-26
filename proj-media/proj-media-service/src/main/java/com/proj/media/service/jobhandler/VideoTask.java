package com.proj.media.service.jobhandler;

import com.proj.base.utils.Mp4VideoUtil;
import com.proj.media.mapper.MediaFilesMapper;
import com.proj.media.model.po.MediaProcess;
import com.proj.media.service.MediaFileProcessService;
import com.proj.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/23 13:33
 */


@Component
@Slf4j
public class VideoTask {
    @Autowired
    MediaFileService mediaFileService;

    @Autowired
    MediaFileProcessService mediaFileProcessService;

//    configuration from nacos
    @Value("${videoprocess.ffmpegpath}")
    String ffmpegpath;


    /**
     * Sharded broadcast task
     */
    @XxlJob("videojobhandler")
    public void shardingJobHandler() throws Exception {

        // Sharding parameters
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

//        fetch pending tasks
        List<MediaProcess> mediaProcessList = null;
        int size = 0;
        try {
//            the number of cpu cores: the number of tasks that can be executed at the same time
            int processors = Runtime.getRuntime().availableProcessors();
            mediaProcessList = mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
            size = mediaProcessList.size();
            log.debug("Fetched {} pending tasks", size);
            if (size < 0) return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

//        Start a thread pool of size threads
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
//        counter
        CountDownLatch countDownLatch = new CountDownLatch(size);
//        put pending task into thread pool
        mediaProcessList.forEach(mediaProcess -> {
            threadPool.execute(() -> {
                try {
                    Long taskId = mediaProcess.getId();
//                    Preempt task
                    boolean b = mediaFileProcessService.startTask(taskId);
                    if (!b) return;
//                    start processing the video
                    String bucket = mediaProcess.getBucket();
                    String filePath = mediaProcess.getFilePath();
                    String fileId = mediaProcess.getFileId();
                    String filename = mediaProcess.getFilename();
//                    download the video to be processed to the server
                    File originalFile = mediaFileService.downloadFileFromMinIO(bucket, filePath);
                    if (originalFile == null) {
                        log.debug("Failed to download file from minio, bucket: {}, filePath: {}", bucket, filePath);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "Failed to download file from minio");
                        return;
                    }

//                    process the video that has been downloaded (.avi --> .mp4)
//                    therefore, mp4File is the file we want after processing
                    File mp4File = null;
                    try {
                        mp4File = File.createTempFile("mp4", ".mp4");
                    } catch (IOException e) {
                        log.error("Failed to create temporary mp4 file");
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "Failed to create temporary mp4 file");
                        return;
                    }

//                    video processing result
                    String result = "";
                    try {
//                        start processing video
                        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegpath, originalFile.getAbsolutePath(), mp4File.getName(), mp4File.getAbsolutePath());
                        result = videoUtil.generateMp4();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Failed to process video: {}, video path: {}, error log: {}", filename, originalFile.getAbsolutePath(), e.getMessage());
                        return;
                    }
                    if (!result.equals("success")) {
                        log.error("Failed to process video, video path: {}, error log: {}", bucket + filePath, result);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                        return;
                    }

//                    upload mp4 file to minio
//                    the storage path of mp4 in minio
                    String objectName = getFilePath(fileId, ".mp4");
                    String url = "/" + bucket + "/" + objectName;
                    try {
                        mediaFileService.addMediaFilesToMinIO(mp4File.getAbsolutePath(), "video/mp4", bucket, objectName);
//                        save url to database, update status to "success", delete pending task and save it into history
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, null);
                    } catch (Exception e) {
                        log.error("Upload video or write into database failure, video path: {}, error log: {}", bucket + objectName, e.getMessage());
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "Upload video or write into database failure");
                    }
                } finally {
//                    latch minus 1
                    countDownLatch.countDown();
                }
            });
        });
//        when latch count reaches 0, unblock
//        Wait, give a sufficient timeout period to prevent infinite waiting.
//        If the timeout period is reached and the processing is not completed, the task will be ended.
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    private String getFilePath(String fileMd5, String fileExt){
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }
}
