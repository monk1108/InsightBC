package com.proj.content.api;

import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Course information editing interface
 * @Author: Yinuo
 * @Date: 2023/10/5 19:31
 */
@Api(value = "Course Info Edit Interface", tags = "Course Info Edit Interface")
@RestController
public class CourseBaseInfoController {

    @ApiOperation("Course Query Interface")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParams){
        CourseBase courseBase = new CourseBase();
        courseBase.setName("测试名称");
        courseBase.setCreateDate(LocalDateTime.now());
        List<CourseBase> courseBases = new ArrayList();
        courseBases.add(courseBase);
        PageResult pageResult = new PageResult<CourseBase>(courseBases,10,1,10);
        return pageResult;
//        return null;

    }

}