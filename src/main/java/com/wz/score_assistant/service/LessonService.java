package com.wz.score_assistant.service;


import com.wz.score_assistant.entity.Lesson;
import com.wz.score_assistant.mapper.LessonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonService {
    @Resource
    private LessonMapper lessonDao;
    public int addLesson(Lesson lesson){
        return lessonDao.insert(lesson);
    }
    public List<Lesson> queryLessonByName(String lesson_name){
        return lessonDao.queryLessonByNameAmbiguously(lesson_name);
    }
    public Lesson queryLessonById(String lesson_id){
        return lessonDao.selectById(lesson_id);
    }
    public List<Lesson> queryAllLesson(){
        return lessonDao.queryAllLesson();
    }

}
