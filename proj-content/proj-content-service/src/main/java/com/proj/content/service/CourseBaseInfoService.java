package com.proj.content.service;

import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.model.dto.AddCourseDto;
import com.proj.content.model.dto.CourseBaseInfoDto;
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

    /**
     * @description Add course basic info
            * @param companyId 
     * @param addCourseDto 
            * @return com.proj.content.model.dto.CourseBaseInfoDto
            * @author Yinuo Yao
            * @date 2023/10/8 17:16:13
            */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
}
