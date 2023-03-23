package com.wz.score_assistant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "课程信息实体类")
@TableName("lessons")
public class Lesson implements Serializable {
    @TableId
    @ApiModelProperty("课程id")
    @JsonProperty(value = "id")
    private  String lessonId;//课程id
    @ApiModelProperty("学期")
    @JsonProperty(value = "term")
    @TableField(exist = false)
    private  String semester;//学期
    @ApiModelProperty("课程名称")
    @JsonProperty(value = "name")
    private  String lessonName;//科目名称
    @ApiModelProperty("修读类型")
    private  String lessonType;//修读类型
    @ApiModelProperty("修读组名")
    private  String lessonGroupName;//修读组名
    @ApiModelProperty("修读组代码")
    private  String lessonGroupCode;//修读组名
    @ApiModelProperty("学分")
    @JsonProperty(value = "credit")
    private  String gradeScore;//学分
    @ApiModelProperty("考核方式")
    @JsonProperty(value = "check")
    private  String checkType;//考核方式

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }
}
