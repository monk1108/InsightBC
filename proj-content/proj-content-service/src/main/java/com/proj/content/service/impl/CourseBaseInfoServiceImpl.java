package com.proj.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.proj.base.exception.ProjException;
import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.mapper.CourseBaseMapper;
import com.proj.content.mapper.CourseCategoryMapper;
import com.proj.content.mapper.CourseMarketMapper;
import com.proj.content.model.dto.AddCourseDto;
import com.proj.content.model.dto.CourseBaseInfoDto;
import com.proj.content.model.dto.EditCourseDto;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import com.proj.content.model.po.CourseCategory;
import com.proj.content.model.po.CourseMarket;
import com.proj.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.time.LocalDateTime;
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
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;

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

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        //合法性校验
//        if (StringUtils.isBlank(dto.getName())) {
////            throw new RuntimeException("Course name is empty!");
//            ProjException.cast("Course name is empty!");
//        }
//
//
//        if (StringUtils.isBlank(dto.getMt())) {
////            throw new RuntimeException("Course category is empty!");
//            ProjException.cast("Course category is empty!");
//        }
//
//        if (StringUtils.isBlank(dto.getSt())) {
////            throw new RuntimeException("Course category is empty!");
//            ProjException.cast("Course category is empty!");
//        }
//
//        if (StringUtils.isBlank(dto.getGrade())) {
////            throw new RuntimeException("Course level is empty!");
//            ProjException.cast("Course level is empty!");
//        }
//
//        if (StringUtils.isBlank(dto.getTeachmode())) {
////            throw new RuntimeException("Education mode is empty!");
//            ProjException.cast("Education mode is empty!");
//        }
//
//        if (StringUtils.isBlank(dto.getUsers())) {
////            throw new RuntimeException("Adaptation user is empty!");
//            ProjException.cast("Adaptation user is empty!");
//        }
//
//        if (StringUtils.isBlank(dto.getCharge())) {
////            throw new RuntimeException("Charging rules are empty!");
//            ProjException.cast("Charging rules are empty!");
//        }
        //新增对象
        CourseBase courseBaseNew = new CourseBase();
        //将填写的课程信息赋值给新增对象
        BeanUtils.copyProperties(dto,courseBaseNew);
        //设置审核状态
        courseBaseNew.setAuditStatus("202002");
        //设置发布状态
        courseBaseNew.setStatus("203001");
        //机构id
        courseBaseNew.setCompanyId(companyId);
        //添加时间
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0){
//            throw new RuntimeException("Failed to add basic course information");
            ProjException.cast("Failed to add basic course information");
        }

        //向课程营销表保存课程营销信息
        //课程营销信息
        CourseMarket courseMarketNew = new CourseMarket();
        Long courseId = courseBaseNew.getId();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseId);
        int i = saveCourseMarket(courseMarketNew);
        if(i<=0){
//            throw new RuntimeException("Failed to save course marketing information");
            ProjException.cast("Failed to save course marketing information");
        }
        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }

    // Save course marketing information
    private int saveCourseMarket(CourseMarket courseMarketNew){
        // charging ruld
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isBlank(charge)){
//            throw new RuntimeException("Haven't choose charging rules");
            ProjException.cast("Haven't choose charging rules");
        }
        // The charging rule is charging
        if(charge.equals("201001")){
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue()<=0){
//                throw new RuntimeException("The course is charged. The price cannot be empty and must be greater than 0.");
                ProjException.cast("The course is charged. The price cannot be empty and must be greater than 0.");
            }
        }
        // Query from course marketing table based on id
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarketNew.getId());
        if(courseMarketObj == null){
            return courseMarketMapper.insert(courseMarketNew);
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }

    // Query the basic information of the course based on the course ID, including basic information and marketing information
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        // Course category name
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;

    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {

        // course Id
        Long courseId = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            ProjException.cast("Course does not exist");
        }

        // Verify that this institution can only modify courses of this institution
        if(!courseBase.getCompanyId().equals(companyId)){
            ProjException.cast("The institution can only modify courses of this institution");
        }

        // Encapsulate basic information data
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        // update course basic information
        int i = courseBaseMapper.updateById(courseBase);

        // update course market information
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);
        saveCourseMarket(courseMarket);
        // Query course basic information and marketing information and return
        CourseBaseInfoDto courseBaseInfo = this.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }
}
