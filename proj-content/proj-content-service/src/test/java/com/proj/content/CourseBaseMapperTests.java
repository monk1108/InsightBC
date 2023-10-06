package com.proj.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.mapper.CourseBaseMapper;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/6 0:20
 */

@SpringBootTest
class CourseBaseMapperTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    void testCourseBaseMapper() {
        CourseBase courseBase = courseBaseMapper.selectById(18L);
        Assertions.assertNotNull(courseBase);

        // Test query interface
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        // Query conditions
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        queryCourseParamsDto.setPublishStatus("203001");

        // Splicing query conditions
        // Fuzzy query based on course name  name like '%name%'
//        like(boolean condition, R column, Object val)
//        column：要用于条件筛选的数据库表列名称，如：name
//        val：用于指定数据表列的值，条件将根据该值进行筛选
//        condition：用于指定当前这个条件是否有效；如果为 true，则应用当前条件；如果为 false，则忽略当前条件。
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName());
        // Based on course review status
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());


        // Paging parameters
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);//页码
        pageParams.setPageSize(3L);//每页记录数
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
    }

}