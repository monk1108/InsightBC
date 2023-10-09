package com.proj.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Course category
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("course_category")
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key
     */
    private String id;

    /**
     * Category Name
     */
    private String name;

    /**
     * category label is the same as name by default
     */
    private String label;

    /**
     * Parent node id
     */
    private String parentid;

    /**
     * display or not
     */
    private Integer isShow;

    /**
     * sort field
     */
    private Integer orderby;

    /**
     * whether is leaf node
     */
    private Integer isLeaf;


}
