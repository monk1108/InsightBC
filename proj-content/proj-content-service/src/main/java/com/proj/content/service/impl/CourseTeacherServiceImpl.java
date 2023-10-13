package com.proj.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.proj.base.exception.ProjException;
import com.proj.content.mapper.CourseTeacherMapper;
import com.proj.content.model.po.CourseTeacher;
import com.proj.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: CourseTeacherServiceImpl
 * @Author: Yinuo
 * @Date: 2023/10/11 22:48
 */

@Service
@Slf4j
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    /**
     * @description get the teacher list of the course
            * @param courseId
            * @return java.util.List<com.proj.content.model.po.CourseTeacher>
            * @author Yinuo Yao
            * @date 2023/10/11 22:53:44
            */
    @Override
    public List<CourseTeacher> getCourseTeacherList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeacherList = courseTeacherMapper.selectList(queryWrapper);
        return courseTeacherList;
    }

    @Override
    @Transactional
    public CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher) {
//        teacher id
        Long teacherId = courseTeacher.getId();
        if (teacherId == null) {
//            add a new teacher to the class
            CourseTeacher teacher = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher, teacher);
            teacher.setCreateDate(LocalDateTime.now());
            int insert = courseTeacherMapper.insert(teacher);
            if (insert <= 0) ProjException.cast("Failed to add a new teacher to the class");
            return getCourseTeacher(teacher);
        }
        else {
//            modify  the teacher information
            CourseTeacher teacher = getCourseTeacher(courseTeacher);
            BeanUtils.copyProperties(courseTeacher, teacher);
            int update = courseTeacherMapper.updateById(teacher);
            if (update <= 0) ProjException.cast("Failed to modify the teacher information");
            return getCourseTeacher(teacher);
        }

    }

    private CourseTeacher getCourseTeacher(CourseTeacher teacher) {
        return courseTeacherMapper.selectById(teacher.getId());
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        queryWrapper.eq(CourseTeacher::getId, teacherId);
        int flag = courseTeacherMapper.delete(queryWrapper);
        if (flag <= 0) ProjException.cast("Failed to delete the teacher from the class");
        log.info("delete course teacher successfully, courseId:{}", courseId);

    }
}
