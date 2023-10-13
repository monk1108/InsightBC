package com.proj.content.service;

import com.proj.content.model.dto.SaveTeachplanDto;
import com.proj.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @Description: Teaching plan management business interface
 * @Author: Yinuo
 * @Date: 2023/10/9 18:18
 */
public interface TeachplanService {
    /**
     * @description Query the course plan tree structure
            * @param courseId
            * @return java.util.List<com.proj.content.model.dto.TeachplanDto>
            * @author Yinuo Yao
            * @date 2023/10/9 18:19:16
            */
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * @description Teach plan creation or updating
            * @param teachplanDto
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/9 18:46:34
            */
    public void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * @description delete teach plan based on teach plan id
            * @param teachplanId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/11 01:12:55
            */
    public void deleteTeachplan(Long teachplanId);

    /**
     * @description Move teach plan up or down
            * @param moveType
     * @param teachplanId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/11 01:57:36
            */
    public void moveTeachPlan(String moveType, Long teachplanId);

}
