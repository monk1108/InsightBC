package com.proj.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: binding media and teach plan
 * @Author: Yinuo
 * @Date: 2023/10/25 14:26
 */

@Data
@ApiModel(value = "BindTeachplanMediaDto", description = "binding media and teach plan")
public class BindTeachplanMediaDto {

    @ApiModelProperty(value = "media file id", required = true)
    private String mediaId;

    @ApiModelProperty(value = "media file name", required = true)
    private String fileName;

    @ApiModelProperty(value = "teach plan id", required = true)
    private Long teachplanId;

}
