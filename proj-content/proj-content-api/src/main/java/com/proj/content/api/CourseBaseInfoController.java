package com.proj.content.api;

import com.proj.base.exception.ValidationGroups;
import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.model.dto.AddCourseDto;
import com.proj.content.model.dto.CourseBaseInfoDto;
import com.proj.content.model.dto.EditCourseDto;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import com.proj.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("Course Paging Query Interface")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto){
        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);

        return courseBasePageResult;

    }

    @ApiOperation("Add basic course information")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto){
        Long companyId = 1232141425L;
        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
    }

    @ApiOperation("Course Query Interface based on course id")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.getCourseBaseInfo(courseId);

        return courseBaseInfoDto;
    }

    @ApiOperation("Update course information")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) EditCourseDto editCourseDto) {
//        Company id
        Long companyId = 1232141425L;
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.updateCourseBase(companyId, editCourseDto);
        return courseBaseInfoDto;
    }

    @DeleteMapping("/course/{courseId}")
    @ApiOperation("Delete course information")
    public void deleteCourseBase(@PathVariable Long courseId) {
        Long companyId = 1232141425L;
        courseBaseInfoService.deleteCourseBase(companyId, courseId);
    }
}