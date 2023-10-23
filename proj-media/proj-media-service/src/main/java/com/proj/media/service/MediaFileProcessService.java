package com.proj.media.service;

import com.proj.media.model.po.MediaProcess;

import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/23 1:01
 */
public interface MediaFileProcessService {
    /***
     * @description fetch pending task
            * @param shardIndex
     * @param shardTotal
     * @param count
            * @return java.util.List<com.proj.media.model.po.MediaProcess>
            * @author Yinuo Yao
            * @date 2023/10/23 01:01:39
            */
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

    /***
     * @description start a task
            * @param id
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/23 01:24:02
            */
    public boolean startTask(long id);

    /***
     * @description save task result
            * @param taskId
     * @param status
     * @param fileId
     * @param url
     * @param errorMsg
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/23 01:26:21
            */
    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);

}
