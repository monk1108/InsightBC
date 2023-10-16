package com.proj.media.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: Upload file to database
 * @Author: Yinuo
 * @Date: 2023/10/15 16:25
 */
@Data
@ToString
public class UploadFileParamsDto {
    /**
     * File name
     */
    private String filename;


    /**
     * File type (doc/image/video)
     */
    private String fileType;
    /**
     * File size
     */
    private Long fileSize;

    /**
     * Tags
     */
    private String tags;

    /**
     * Uploader
     */
    private String username;

    /**
     * Comment
     */
    private String remark;

}
