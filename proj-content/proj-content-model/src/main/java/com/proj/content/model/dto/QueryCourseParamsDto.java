package com.proj.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @description Course query parameter Dto
 * @author Yinuo Yao
 * @date 2023/10/05
 * @version 1.0
 */
@Data
@ToString
public class QueryCourseParamsDto {

    // Approval Status
    private String auditStatus;
    // Course Name
    private String courseName;
    // Post status
    private String publishStatus;

}