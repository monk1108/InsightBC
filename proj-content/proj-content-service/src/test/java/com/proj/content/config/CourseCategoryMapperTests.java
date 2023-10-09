package com.proj.content.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.proj.base.model.PageParams;
import com.proj.base.model.PageResult;
import com.proj.content.mapper.CourseBaseMapper;
import com.proj.content.mapper.CourseCategoryMapper;
import com.proj.content.model.dto.CourseCategoryTreeDto;
import com.proj.content.model.dto.QueryCourseParamsDto;
import com.proj.content.model.po.CourseBase;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.transform.Source;
import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/6 0:20
 */

@SpringBootTest
class CourseCategoryMapperTests {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Test
    void testCourseCategoryMapper() {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes("1-9");
        System.out.println(courseCategoryTreeDtos);
    }

}