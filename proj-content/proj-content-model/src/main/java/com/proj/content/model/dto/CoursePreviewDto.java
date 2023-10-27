package com.proj.content.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.swing.*;
import java.util.List;

/**
 * @Description: for course preview
 * @Author: Yinuo
 * @Date: 2023/10/26 18:04
 */

@Data
@ToString
public class CoursePreviewDto {

    CourseBaseInfoDto courseBase;

    // teach plan (chapters)
    List<TeachplanDto> teachplans;
}
