package com.proj.content.model.dto;

import com.proj.base.exception.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @description Add course dto
        * @author Yinuo Yao
        * @date 2023/10/8 17:06:43
        */
@Data
@ApiModel(value="AddCourseDto", description="Add course basic info")
public class AddCourseDto {

 @NotEmpty(message = "Course name cannot be blank when adding a course", groups = ValidationGroups.Insert.class)
 @NotEmpty(message = "Course name cannot be blank when updating a course", groups = ValidationGroups.Update.class)
 @ApiModelProperty(value = "Course name", required = true)
 private String name;

 @NotEmpty(message = "Applicable user cannot be blank")
 @Size(message = "Applicable user info not enough",min = 10)
 @ApiModelProperty(value = "Applicable user", required = true)
 private String users;

 @ApiModelProperty(value = "Course label")
 private String tags;

 @NotEmpty(message = "Course category cannot be blank")
 @ApiModelProperty(value = "Major category", required = true)
 private String mt;

 @NotEmpty(message = "Course category cannot be blank")
 @ApiModelProperty(value = "Minor category", required = true)
 private String st;

 @NotEmpty(message = "Course level cannot be blank")
 @ApiModelProperty(value = "Course level", required = true)
 private String grade;

 @ApiModelProperty(value = "Teaching mode (normal, recording, live broadcast, etc.)", required = true)
 private String teachmode;

 @ApiModelProperty(value = "Course introduction")
 @Size(message = "Course introduction too short",min = 10)
 private String description;

 @ApiModelProperty(value = "Course image", required = true)
 private String pic;

 @NotEmpty(message = "Charging rules cannot be empty")
 @ApiModelProperty(value = "Charging rules, corresponding data dictionary", required = true)
 private String charge;

 @ApiModelProperty(value = "Price")
 private Float price;
 @ApiModelProperty(value = "Original price")
 private Float originalPrice;


 @ApiModelProperty(value = "qq")
 private String qq;

 @ApiModelProperty(value = "Wechat")
 private String wechat;
 @ApiModelProperty(value = "Tele")
 private String phone;

 @ApiModelProperty(value = "Validity period")
 private Integer validDays;
}
