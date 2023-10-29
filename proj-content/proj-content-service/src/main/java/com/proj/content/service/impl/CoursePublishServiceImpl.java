package com.proj.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.proj.base.exception.CommonError;
import com.proj.base.exception.ProjException;
import com.proj.content.mapper.*;
import com.proj.content.model.dto.CourseBaseInfoDto;
import com.proj.content.model.dto.CoursePreviewDto;
import com.proj.content.model.dto.TeachplanDto;
import com.proj.content.model.po.CourseBase;
import com.proj.content.model.po.CourseMarket;
import com.proj.content.model.po.CoursePublish;
import com.proj.content.model.po.CoursePublishPre;
import com.proj.content.service.CourseBaseInfoService;
import com.proj.content.service.CoursePublishService;
import com.proj.content.service.TeachplanService;
import com.proj.messagesdk.model.po.MqMessage;
import com.proj.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/26 18:11
 */
@Service
@Slf4j
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @Autowired
    TeachplanService teachplanService;

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CoursePublishPreMapper coursePublishPreMapper;

    @Autowired
    CoursePublishMapper coursePublishMapper;

    @Autowired
    MqMessageService mqMessageService;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        coursePreviewDto.setCourseBase(courseBaseInfo);

        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        coursePreviewDto.setTeachplans(teachplanTree);

        return coursePreviewDto;

    }

    @Override
    @Transactional
    public void commitAudit(Long companyId, Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        String auditStatus = courseBase.getAuditStatus();
//        if audit status is "submitted", duplicated submit is not allowed
        if ("202003".equals(auditStatus)) {
            ProjException.cast("It is currently waiting for review. Once the review is completed, you can submit it again.");

        }
//        one institution can only submit the course owned by itself
        if (!companyId.equals(courseBase.getCompanyId())) {
            ProjException.cast("You are not authorized to submit other institutions' course.");
        }

//        course image should be put
        if (StringUtils.isEmpty(courseBase.getPic())) {
            ProjException.cast("Course image is required.");
        }
//        teach plan shouldn't be empty
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        if (teachplanTree == null || teachplanTree.size() == 0) {
            ProjException.cast("Course plan is required.");
        }

//        course prepublish info
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);


//        course marketing info
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        String courseMarketJson = JSON.toJSONString(courseMarket);
        coursePublishPre.setMarket(courseMarketJson);

//        teach plan info
        String teachplanTreeString = JSON.toJSONString(teachplanTree);
        coursePublishPre.setTeachplan(teachplanTreeString);

//        set course status to pre-submit
        coursePublishPre.setStatus("202003");
        coursePublishPre.setCompanyId(companyId);
        coursePublishPre.setCreateDate(LocalDateTime.now());
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreUpdate == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);

    }

    @Transactional
    @Override
    public void publish(Long companyId, Long courseId) {
//        course info in course pre-publish table
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            ProjException.cast("Please submit ther course for audit first, after that course can be published!");
        }
//       one institute can only publish the course owned by itself
        if (!companyId.equals(coursePublishPre.getCompanyId())) {
            ProjException.cast("You are not authorized to publish other institutions' course.");
        }

//        course audit status
        String auditStatus = coursePublishPre.getStatus();
//        only courses that have been approved can be published
        if (!"202004".equals(auditStatus)) {
            ProjException.cast("Only courses that have been approved can be published!");
        }
        
//        save course publish info to course publish table
        saveCoursePublish(courseId);

//        save message queue table
        saveCoursePublishMessage(courseId);

//        delete course pre-publish info
        coursePublishPreMapper.deleteById(courseId);
    }

    /**
     * @description save course publish info to course publish table
            * @param courseId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/27 01:34:57
            */
    private void saveCoursePublish(Long courseId) {
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            ProjException.cast("Please submit ther course for audit first, after that course can be published!");
        }

//        dto that's to be written into course publish table
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        coursePublish.setStatus("203002");
        CoursePublish coursePublishUpdate = coursePublishMapper.selectById(courseId);
        if (coursePublishUpdate == null) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }

//        update course info in course base table
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        courseBase.setStatus("203002");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * @description save course info into message queue table
            * @param courseId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/27 01:35:04
            */
    private void saveCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
        if (mqMessage == null) {
            ProjException.cast(CommonError.UNKOWN_ERROR);
        }
    }
}
