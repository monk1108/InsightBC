package com.proj.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.proj.base.exception.ProjException;
import com.proj.content.mapper.TeachplanMapper;
import com.proj.content.mapper.TeachplanMediaMapper;
import com.proj.content.model.dto.SaveTeachplanDto;
import com.proj.content.model.dto.TeachplanDto;
import com.proj.content.model.po.Teachplan;
import com.proj.content.model.po.TeachplanMedia;
import com.proj.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: Course PLan Service Impl
 * @Author: Yinuo
 * @Date: 2023/10/9 18:20
 */
@Service
@Slf4j
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        // Teach plan id
        Long id = teachplanDto.getId();
        // id exists, update the course
        if(id != null){
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.updateById(teachplan);
        }
        else{
//            id doesn't exist, create a new one
            // Get the number of lesson plans of the same parent and the same level
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            teachplanNew.setCreateDate(LocalDateTime.now());
            // set teach plan order
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);
    }
}

    @Override
    public void deleteTeachplan(Long teachplanId) {
        if (teachplanId == null) {
            ProjException.cast("Teach plan id is required");
        }
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        Integer grade = teachplan.getGrade();

//        chapter
        if (grade == 1) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, teachplanId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            if (count > 0) {
//                there's sections in this chapter
                ProjException.cast("There's sections in this chapter");
            }
            teachplanMapper.deleteById(teachplanId);
        }
//        section
        else {
            teachplanMapper.deleteById(teachplanId);
//            also delete corresponding teach plan media
            LambdaQueryWrapper<TeachplanMedia> mediaLambdaQueryWrapper = new LambdaQueryWrapper<TeachplanMedia>()
                    .eq(TeachplanMedia::getTeachplanId, teachplanId);
            teachplanMediaMapper.delete(mediaLambdaQueryWrapper);
        }
    }

    @Transactional
    @Override
    public void moveTeachPlan(String moveType, Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        Integer grade = teachplan.getGrade();
        Integer orderby = teachplan.getOrderby();
        Long courseId = teachplan.getCourseId();
        Long parentId = teachplan.getParentid();

        if ("moveup".equals(moveType)) {
            if (grade == 1) {
//                chapter
                Teachplan teachplan1 = teachplanMapper.selectOne(new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getGrade, 1)
                        .eq(Teachplan::getCourseId, courseId)
                        .lt(Teachplan::getOrderby, orderby)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1"));
                exchangeOrderBy(teachplan, teachplan1);
            }
            else if (grade == 2) {
//                section
                Teachplan teachplan1 = teachplanMapper.selectOne(new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getGrade, 2)
                        .eq(Teachplan::getParentid, parentId)
                        .lt(Teachplan::getOrderby, orderby)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1"));
                exchangeOrderBy(teachplan, teachplan1);
            }
        }
        else if ("movedown".equals(moveType)) {
            if (grade == 1) {
//                chapter
                Teachplan teachplan1 = teachplanMapper.selectOne(new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getGrade, 1)
                        .eq(Teachplan::getCourseId, courseId)
                        .gt(Teachplan::getOrderby, orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1"));
                exchangeOrderBy(teachplan, teachplan1);
            }
            else if (grade == 2) {
//                section
                Teachplan teachplan1 = teachplanMapper.selectOne(new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getGrade, 2)
                        .eq(Teachplan::getParentid, parentId)
                        .gt(Teachplan::getOrderby, orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1"));
                exchangeOrderBy(teachplan, teachplan1);
            }
        }


    }

    public void exchangeOrderBy(Teachplan a, Teachplan b) {
        if (b == null) ProjException.cast("Can't shift anymore!");
        Integer aOrderby = a.getOrderby();
        a.setOrderby(b.getOrderby());
        b.setOrderby(aOrderby);
        teachplanMapper.updateById(a);
        teachplanMapper.updateById(b);
    }


    /**
     * @description get new teach plan id
            * @param courseId
     * @param parentid
            * @return int
            * @author Yinuo Yao
            * @date 2023/10/9 18:50:54
            */
    private int getTeachplanCount(Long courseId, Long parentid) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<Teachplan>()
                .eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentid);
        int count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }
    }
