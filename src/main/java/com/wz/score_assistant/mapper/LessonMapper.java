package com.wz.score_assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wz.score_assistant.entity.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LessonMapper extends BaseMapper<Lesson> {
    List<Lesson> queryAllLesson();

    List<Lesson> queryLessonByNameAmbiguously(String lesson_name);
}
