package com.proj.content.api;

import com.proj.content.model.dto.CourseCategoryTreeDto;
import com.proj.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: Data dictionary front-end controller
 * @Author: Yinuo
 * @Date: 2023/10/8 15:55
 */
@RestController
@Slf4j
public class CourseCategoryController {
    @Autowired
    CourseCategoryService courseCategoryService;

    @GetMapping("course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }
}
