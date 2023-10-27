package com.proj.content.service;

import com.proj.content.model.dto.CoursePreviewDto;

/**
 * @Description: interface for course preview and release
 * @Author: Yinuo
 * @Date: 2023/10/26 18:08
 */
public interface CoursePublishService {

    /***
     * @description get course preview info
            * @param courseId
            * @return com.proj.content.model.dto.CoursePreviewDto
            * @author Yinuo Yao
            * @date 2023/10/26 18:09:19
            */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /***
     * @description course audit
            * @param companyId
     * @param courseId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/26 22:07:46
            */
    public void commitAudit(Long companyId,Long courseId);
}
