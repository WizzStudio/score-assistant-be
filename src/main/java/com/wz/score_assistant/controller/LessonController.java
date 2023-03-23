package com.wz.score_assistant.controller;

import com.wz.score_assistant.service.LessonService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "课程信息接口",description = "拥有搜索课程的功能")
@RequestMapping("/lesson")
public class LessonController {
    @Resource
    LessonService lessonService;

//    @ApiOperation(value = "搜索功能-搜索课程",notes = "例如要搜索军事理论，则input参数填军，就会返回军事理论与军事训练的详细信息",httpMethod = "GET")
//    @GetMapping(value = "/namesLikeInput/{input}",produces = "application/json;charset=utf-8")
//    public Score_data namesLikeInput(@PathVariable String input){
//        List<Lesson> lessons = lessonService.queryLessonByName(input);
//        Score_data data = new Score_data();
//        List<Course> courses = new ArrayList<>()
//        data.setCourses(courses);
//    }

}
