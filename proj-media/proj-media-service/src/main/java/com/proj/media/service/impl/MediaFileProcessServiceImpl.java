package com.proj.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.proj.media.mapper.MediaFilesMapper;
import com.proj.media.mapper.MediaProcessHistoryMapper;
import com.proj.media.mapper.MediaProcessMapper;
import com.proj.media.model.po.MediaFiles;
import com.proj.media.model.po.MediaProcess;
import com.proj.media.model.po.MediaProcessHistory;
import com.proj.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/23 1:02
 */
@Service
@Slf4j
public class MediaFileProcessServiceImpl implements MediaFileProcessService{
    @Autowired
    MediaFilesMapper mediaFilesMapper;

    @Autowired
    MediaProcessMapper mediaProcessMapper;

    @Autowired
    MediaProcessHistoryMapper  mediaProcessHistoryMapper;

    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        List<MediaProcess> mediaProcesses = mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
        return mediaProcesses;
    }

    @Override
    public boolean startTask(long id) {
        int result = mediaProcessMapper.startTask(id);
        return result <= 0 ? false : true;
    }

    @Transactional
    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
//        the task to be updated
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if (mediaProcess == null) {
            return;
        }
//        if task failed, update the info in table 'media_process'
        LambdaQueryWrapper<MediaProcess> queryWrapper = new LambdaQueryWrapper<MediaProcess>()
                .eq(MediaProcess::getId, taskId);
        if (status.equals("3")) {
            MediaProcess mediaProcess_u = new MediaProcess();
            mediaProcess_u.setStatus("3");
            mediaProcess_u.setErrormsg(errorMsg);
            mediaProcess_u.setFailCount(mediaProcess.getFailCount() + 1);
            mediaProcessMapper.update(mediaProcess_u, queryWrapper);
            log.debug("Updating task status failed, task info: {}", mediaProcess_u);
            return;
        }

//        task succeeded
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
//        update file url in table 'media_files'
        if (mediaFiles != null) {
            mediaFiles.setUrl(url);
            mediaFilesMapper.updateById(mediaFiles);
        }
//        update in table 'media_process'
        mediaProcess.setUrl(url);
        mediaProcess.setStatus(status);
        mediaProcess.setFinishDate(LocalDateTime.now());
        mediaProcessMapper.updateById(mediaProcess);

//        add into media history
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);
//        delete file info from table 'media_process'
        mediaProcessMapper.deleteById(mediaProcess.getId());

    }
}
