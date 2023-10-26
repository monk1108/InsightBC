package com.proj.content.api;

import com.proj.content.model.dto.BindTeachplanMediaDto;
import com.proj.content.model.dto.SaveTeachplanDto;
import com.proj.content.model.dto.TeachplanDto;
import com.proj.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/9 17:44
 */
@RestController
@Api(value = "Course plan editing interface",tags = "Course plan editing interface")
public class TeachplanController {
    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("Course plan query tree structure")
    @ApiImplicitParam(value = "courseId",name = "course Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("Teach plan creation or updating")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan) {
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("Delete course plan")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId) {
        teachplanService.deleteTeachplan(teachplanId);
    }

    @ApiOperation("Move course plan up or down")
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void moveTeachPlan(@PathVariable String moveType, @PathVariable Long teachplanId) {
        teachplanService.moveTeachPlan(moveType, teachplanId);
    }

    @ApiOperation(value = "binding media file and teach plan")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation(value = "unassociate media and teach plan")
    @DeleteMapping("/teachplan/association/media/{teachplanId}/{mediaId}")
    public void unassociationMedia(@PathVariable Long teachplanId, @PathVariable String mediaId) {
        teachplanService.unassociationMedia(teachplanId, mediaId);
    }
}
