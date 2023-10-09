package com.proj.content.service.impl;

import com.proj.content.mapper.CourseCategoryMapper;
import com.proj.content.model.dto.CourseCategoryTreeDto;
import com.proj.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/8 16:32
 */

@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        // convert list to map, excluding root node
//            (key1, key2) -> key2): avoid duplicates
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream().
                filter(item->!id.equals(item.getId())).
                collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        // list to be returned
        List<CourseCategoryTreeDto> categoryTreeDtos = new ArrayList<>();
        // iterate over every element, excluding root node
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            if(item.getParentid().equals(id)){
                categoryTreeDtos.add(item);
            }
            // find parent node
//                set parent node's ChildrenTreeNodes property
            CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
            if(courseCategoryTreeDto!=null){
                if(courseCategoryTreeDto.getChildrenTreeNodes() ==null){
                    courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                // put child node into ChildrenTreeNodes
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
            }
        });
        return categoryTreeDtos;
    }

}
