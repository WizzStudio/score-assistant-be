package com.wz.score_assistant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "培养方案实体类")
@TableName("lesson_plan")
public class Plan implements Serializable {
    @TableId
    @ApiModelProperty("方案id")
    @TableField(value = "plan_id")
    private int planId;
    @ApiModelProperty("年级")
    @TableField(value = "grade")
    private int grade;
    @ApiModelProperty("具体数据地址")
    @TableField(value = "plan_data")
    private String planData;
    @ApiModelProperty("方案代码")
    @TableField(value = "plan_code")
    private String planCode;
    @ApiModelProperty("方案名称")
    @TableField(value = "plan_name")
    private String planName;
    @ApiModelProperty("方案课程")
    @TableField(exist = false)
    private List<Lesson> lessonList;

    public Plan(int grade, String planData, String planName) {
        this.grade = grade;
        this.planData = planData;
        this.planName = planName;
    }
}
