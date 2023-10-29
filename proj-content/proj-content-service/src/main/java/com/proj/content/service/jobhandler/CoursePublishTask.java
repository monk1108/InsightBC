package com.proj.content.service.jobhandler;

import com.proj.messagesdk.model.po.MqMessage;
import com.proj.messagesdk.service.MessageProcessAbstract;
import com.proj.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/27 17:25
 */

@Component
@Slf4j
public class CoursePublishTask extends MessageProcessAbstract {

    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() throws Exception {
        // shard parameter
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.debug("shardIndex = "+shardIndex+",shardTotal = "+shardTotal);
        //参数:分片序号、分片总数、消息类型、一次最多取到的任务数量、一次任务调度执行的超时时间
        process(shardIndex, shardTotal, "course_publish", 30, 60);
    }



//    Implement course publish task
    @Override
    public boolean execute(MqMessage mqMessage) {
//        get course info from mqmessage
//        course id
        String businessKey1 = mqMessage.getBusinessKey1();
        int courseId = Integer.parseInt(businessKey1);

//        upload course html to minio
        generateCourseHtml(mqMessage, courseId);
//        write course index info into elasticsearch
        saveCourseIndex(mqMessage, courseId);
//        write cache into redis
        saveCourseCache(mqMessage, courseId);
//        return true, indicating the finish of the 3 tasks
        return true;
    }

    // write cache into redis
    private void saveCourseCache(MqMessage mqMessage, int courseId) {
        log.debug("Writing course cache to redis, courseId:{}",  courseId);
//        message id
        Long id = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
//        get task stage 3 to ensure idempotence of the task
        int stageThree = mqMessageService.getStageThree(id);
        if (stageThree > 0) {
            log.debug("The task has been executed, courseId:{}", courseId);
            return;
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        mark task stage 3 as completed
        mqMessageService.completedStageThree(id);
    }


    // write course index info into elasticsearch
    private void saveCourseIndex(MqMessage mqMessage, int courseId) {
        log.debug("write course index info into elasticsearch, courseId:{}", courseId);
//        message id
        Long id = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
//        get task stage 2 to ensure idempotence of the task
        int stageTwo = mqMessageService.getStageTwo(id);
        if (stageTwo > 0) {
            log.debug("The task has been executed, courseId:{}", courseId);
            return;
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        mark task stage 2 as completed
        mqMessageService.completedStageTwo(id);
    }


    // upload course html to minio
    private void generateCourseHtml(MqMessage mqMessage, int courseId) {
        log.debug("Starting generate course html to minio, courseId:{}", courseId);
//        message id
        Long id = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
//        get task stage 1 to ensure idempotence of the task
        int stageOne = mqMessageService.getStageOne(id);
        if (stageOne > 0) {
            log.debug("The task has been executed, courseId:{}", courseId);
            return;
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        mark task stage 1 as completed
        mqMessageService.completedStageOne(id);
    }
}
