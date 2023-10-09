package com.proj.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.proj.content.model.dto.CourseCategoryTreeDto;
import com.proj.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * Course category mapper interface
 * </p>
 *
 * @author Yinuo Yao
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
