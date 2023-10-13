package com.proj.content;

import com.proj.content.mapper.TeachplanMapper;
import com.proj.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/9 18:13
 */
@SpringBootTest
public class TeachPlanMapperTests {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Test
    public void testSelectTreeNodes() {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(117L);
        System.out.println(teachplanDtos);
    }
}
