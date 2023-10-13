package com.proj.content.service;

import com.proj.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @Description: course teacher service
 * @Author: Yinuo
 * @Date: 2023/10/11 22:46
 */
public interface CourseTeacherService {
    List<CourseTeacher> getCourseTeacherList(Long courseId);
    CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher);
    void deleteCourseTeacher(Long courseId, Long teacherId);
}
