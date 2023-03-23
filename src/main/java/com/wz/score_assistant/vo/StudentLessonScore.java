package com.wz.score_assistant.vo;

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
@ApiModel(description = "学生课程成绩实体类")
public class StudentLessonScore implements Serializable {
    @ApiModelProperty("课程id")
    private String lessonId;//课程id

     @ApiModelProperty("考试时间")
    private String testDate;//考试时间
    @ApiModelProperty("学期")
    private String semester;//学期
    @ApiModelProperty("修读类型")
    private String lessonType;//修读类型
    @ApiModelProperty("修读组")
    private String lessonGroupName;//修读组
    @ApiModelProperty("科目名称")
    private String lessonName;//科目名称
    @ApiModelProperty("分数")
    private String studentScore;//分数
    @ApiModelProperty("学分")
    private String gradeScore;//学分
    @ApiModelProperty("是否及格")
    private String ifQualified;//是否及格
    @ApiModelProperty("考核方式")
    private String checkType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentLessonScore that = (StudentLessonScore) o;
        return Objects.equals(lessonId, that.lessonId) && Objects.equals(semester, that.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, semester);
    }

    public StudentLessonScore(StudentLessonScore studentLessonScore) {
        this.lessonId = studentLessonScore.getLessonId();
        this.semester = studentLessonScore.getSemester();
        this.lessonName = studentLessonScore.getLessonName();
        this.lessonType = studentLessonScore.getLessonType();
        this.lessonGroupName = studentLessonScore.getLessonGroupName();
        this.studentScore = studentLessonScore.getStudentScore();
        this.gradeScore = studentLessonScore.getGradeScore();
        this.ifQualified = studentLessonScore.getIfQualified();
        this.testDate = studentLessonScore.getTestDate();
    }
}
