package com.wz.score_assistant;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wz.score_assistant.controller.PlanController;
import com.wz.score_assistant.controller.UserController;
import com.wz.score_assistant.entity.Lesson;
import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.entity.User;
import com.wz.score_assistant.service.PlanService;
import com.wz.score_assistant.utils.FileUtilsImpl;
import com.wz.score_assistant.vo.StudentLessonScore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ScoreAssistantApplicationTests {
//    @Resource
//    PlanService planService;
//    @Resource
//    FileUtilsImpl fileUtils;
//    @Resource
//    UserController userController;
//    @Resource
//    PlanController planController;
//
//    @Test
//    void contextLoads() {
//        User user = new User() {{
//            setUserId("20009101942");
//            setPassword("MjcxODI4MTgyOC4=");
//        }};
//        List<StudentLessonScore> data4User = userController.getData4User(user);
//        HashMap<String, Integer> set = new HashMap<>();
//        for (StudentLessonScore s :
//                data4User) {
//            set.put(s.getLessonId(), set.getOrDefault(s.getLessonId(), 0) + 1);
//        }
//        for (Map.Entry<String, Integer> e : set.entrySet()) {
//            if (e.getValue() > 1) System.out.println(e.getKey() + ":" + e.getValue());
//        }
////        System.out.println(fileUtils.getBASE_PATH());
//    }
//
//    @Test
//    void contextLoads2() {
//        List<Lesson> list = planController.queryLessonAll(351);
//        for (Lesson l : list) {
//            if (l.getSemester().length() > 15) {
//                System.out.println(l);
//            }
//        }
//    }
//
//    @Test
//    void contextLoads3() {
//        for (int i = 1; i < 422; i++) {
//            Plan plan = planService.queryPlanById(i);
//            List<Lesson> list = planController.queryLessonAll(i);
//            System.out.println(i);
//            list = userController.specialJudge(list);
////            FileReader fileReader = new FileReader("C:\\Users\\15004\\Desktop\\final_project\\score_assistant\\src\\main\\resources\\clean_data\\"+plan.getPlanData());
////            JSONObject jsonObject = JSON.parseObject(fileReader.readString());
////            jsonObject.put("row",list);
////            System.out.println(jsonObject.toJSONString());
////            new Scanner(System.in).next();
////            fileUtils.writeFile("C:\\Users\\15004\\Desktop\\final_project\\score_assistant\\src\\main\\resources\\clean_data\\"+plan.getPlanData(),jsonObject.toJSONString());
//        }
//    }

}
