package com.proj.media.api;

import com.proj.base.exception.ProjException;
import com.proj.base.model.RestResponse;
import com.proj.media.model.po.MediaFiles;
import com.proj.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/26 19:20
 */
@Api(value = "Media file management interface",tags = "Media file management interface")
@RestController
@RequestMapping("/open")
public class MediaOpenController {
    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation("Preview video")
    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId){

        MediaFiles mediaFiles = mediaFileService.getFilesById(mediaId);
        if(mediaFiles == null || StringUtils.isEmpty(mediaFiles.getUrl())){
            ProjException.cast("The video hasn't been transcoded yet.");
        }
        return RestResponse.success(mediaFiles.getUrl());

    }
}
