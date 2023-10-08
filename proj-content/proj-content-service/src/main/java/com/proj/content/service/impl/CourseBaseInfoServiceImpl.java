package com.proj.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.mapper.CourseBaseMapper;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import com.proj.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: Course Basic Information Management Class
 * @Author: Yinuo
 * @Date: 2023/10/6 14:10
 */

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();


        // Splicing query conditions
        // Fuzzy query based on course name  name like '%name%'
//        like(boolean condition, R column, Object val)
//        column：要用于条件筛选的数据库表列名称，如：name
//        val：用于指定数据表列的值，条件将根据该值进行筛选
//        condition：用于指定当前这个条件是否有效；如果为 true，则应用当前条件；如果为 false，则忽略当前条件。
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName());
        // Based on course review status
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParamsDto.getAuditStatus());

        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()), CourseBase::getStatus, queryCourseParamsDto.getPublishStatus());
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        // Paging queryE page paging parameters, @Param("ew") Wrapper<T> queryWrapper
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        // data list
        List<CourseBase> items = pageResult.getRecords();
        // total query nums
        long total = pageResult.getTotal();

        // return data
        // List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());
        System.out.println(courseBasePageResult);
        return courseBasePageResult;
    }
}
