package com.proj.content.service;

import com.proj.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/8 16:31
 */
public interface CourseCategoryService {
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
