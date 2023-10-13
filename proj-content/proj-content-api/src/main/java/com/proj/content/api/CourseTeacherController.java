package com.proj.content.api;

import com.proj.content.model.po.CourseTeacher;
import com.proj.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: course teacher controller
 * @Author: Yinuo
 * @Date: 2023/10/11 22:36
 */
@RestController
@Slf4j
@Api(value = "Course teacher interface",tags = "Course teacher interface")
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;

    @GetMapping("/courseTeacher/list/{courseId}")
    @ApiOperation("get course teacher list")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable Long courseId){
        return courseTeacherService.getCourseTeacherList(courseId);
    }

    @PostMapping("/courseTeacher")
    @ApiOperation("add/ update course teacher")
    public CourseTeacher saveCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.saveCourseTeacher(courseTeacher);
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    @ApiOperation("delete course teacher")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long teacherId){
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }
}
