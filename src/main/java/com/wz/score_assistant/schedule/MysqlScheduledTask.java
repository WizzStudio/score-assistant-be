package com.wz.score_assistant.schedule;

import com.wz.score_assistant.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MysqlScheduledTask {

    @Autowired
    private LessonService lessonService;

    @Scheduled(fixedRate = 6000000) // 每5秒执行一次
    public void visDB() {
        try{
            lessonService.queryAllLesson();
            log.info("访问数据库成功");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
