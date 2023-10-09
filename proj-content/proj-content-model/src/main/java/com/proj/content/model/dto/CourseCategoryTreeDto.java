package com.proj.content.model.dto;

import com.proj.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @Description: tree node for course category
 * @Author: Yinuo
 * @Date: 2023/10/8 15:51
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable{
    List<CourseCategoryTreeDto> childrenTreeNodes;


}
