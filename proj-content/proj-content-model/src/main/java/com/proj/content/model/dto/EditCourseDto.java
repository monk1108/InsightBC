package com.proj.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/9 16:41
 */
@Data
@ApiModel(value="EditCourseDto", description="Modify course information")
public class EditCourseDto extends AddCourseDto{

    @ApiModelProperty(value = "Course ID", required = true)
    private Long id;


}
