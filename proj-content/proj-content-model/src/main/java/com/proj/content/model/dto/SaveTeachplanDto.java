package com.proj.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: Save course plan dto, including new additions and modifications
 * @Author: Yinuo
 * @Date: 2023/10/9 18:40
 */
@Data
@ToString
public class SaveTeachplanDto {
    /***
     * Teach plan id
     */
    private Long id;

    /**
     * Teach plan name
     */
    private String pname;

    /**
     * Teach plan parent id
     */
    private Long parentid;

    /**
     * Grade of the course (1, 2, 3)
     */
    private Integer grade;

    /**
     * Course media type (1. Video, 2. Document)
     */
    private String mediaType;


    /**
     * course id
     */
    private Long courseId;

    /**
     * Course public id
     */
    private Long coursePubId;


    /**
     * whether supports previewing the course
     */
    private String isPreview;
}
