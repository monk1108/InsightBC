package com.proj.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.proj.content.model.dto.TeachplanDto;
import com.proj.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * Course plan Mapper interface
 * </p>
 *
 * @author Yinuo Yao
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    public List<TeachplanDto> selectTreeNodes(long courseId);

}
