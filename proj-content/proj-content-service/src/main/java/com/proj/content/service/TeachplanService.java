package com.proj.content.service;

import com.proj.content.model.dto.BindTeachplanMediaDto;
import com.proj.content.model.dto.SaveTeachplanDto;
import com.proj.content.model.dto.TeachplanDto;
import com.proj.content.model.po.TeachplanMedia;

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

    /***
     * @description binding media file and teach plan
            * @param bindTeachplanMediaDto
            * @return com.proj.content.model.po.TeachplanMedia
            * @author Yinuo Yao
            * @date 2023/10/25 14:45:40
            */
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /***
     * @description unbinding media file and teach plan
            * @param teachPlanId
     * @param mediaId
            * @return void
            * @author Yinuo Yao
            * @date 2023/10/25 15:16:18
            */
    public void unassociationMedia(Long teachPlanId, String mediaId);
}
