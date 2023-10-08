package com.proj.content.service;

import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;

/**
 * @Description: Course Basic Information Management Interface
 * @Author: Yinuo
 * @Date: 2023/10/6 14:04
 */
public interface CourseBaseInfoService {
    /**
     * @param pageParams
     * @param queryCourseParamsDto
     * @return com.proj.base.model.PageResult<com.proj.content.model.po.CourseBase>
     * @description Course page query
     * @author Yinuo Yao
     * @date 2023/10/6 14:31:26
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
