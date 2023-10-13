package com.proj.content.model.dto;

import com.proj.content.model.po.Teachplan;
import com.proj.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: Course plan tree structure dto
 * @Author: Yinuo
 * @Date: 2023/10/9 17:38
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {
    // Media information associated with lesson plans
    TeachplanMedia teachplanMedia;

    // child node (subordinate chapters)
    List<TeachplanDto> teachPlanTreeNodes;
}
