package com.wz.score_assistant.controller;//package com.wz.score_assistant.controller;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.wz.score_assistant.entity.Lesson;
import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.entity.User;
import com.wz.score_assistant.exception.APIException;
import com.wz.score_assistant.service.PlanService;
import com.wz.score_assistant.service.UserService;
import com.wz.score_assistant.utils.FileUtils;
import com.wz.score_assistant.vo.AppCode;
import com.wz.score_assistant.vo.StudentLessonScore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户交互接口",description = "包含用户登录,进行成绩查询,与方案匹配等功能")
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    PlanService planService;
    @Resource
    FileUtils fileUtils;
    @ApiOperation(value = "做登录顺便进行成绩查询", notes = "返回成绩列表", httpMethod = "POST")
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    @Transactional
    public List<StudentLessonScore> doLogin( @ApiIgnore HttpSession session, String account, String password) {
        if(StrUtil.isBlank(account)||StrUtil.isBlank((password)))throw new APIException(AppCode.APP_ERROR,"登录异常");
        String user_id=account;
        User user = new User();
        user_id = user_id.trim();
        password = Base64Encoder.encode(password.trim().getBytes()).toString();
        user.setUserId(user_id);
        user.setPassword(password);
        List<StudentLessonScore> data = getData4User(user);
        session.setAttribute("userLoginInfo", UUID.randomUUID() + "_" + user_id+"_"+ password);
        return data;
    }
    @ApiOperation(value = "按照培养方案的id获取里面所有课程组名名中包含group的未修读课程",notes = "当type参数为all时返回所有课程，例如你要查找所有课组名为专业选修课的课程，那么group就填专业选修课",httpMethod = "GET")
    @GetMapping(value = "/queryL/{majorPlanId}/{group}",produces = "application/json;charset=utf-8")
    public List<StudentLessonScore> queryLessonByGroup(@ApiIgnore HttpSession session,@PathVariable String group, @PathVariable int majorPlanId){
        List<StudentLessonScore> list = matchData(session, majorPlanId);
        if (!group.equals("all")){
        list = list.stream().filter(studentLessonScore -> {
            if (studentLessonScore.getLessonGroupName().contains(group))
                return true;
            return false;
        }).collect(Collectors.toList());
        }
        return list;
    }
//    public List<Lesson> queryLessonByGroup(@ApiIgnore HttpSession session,@PathVariable String group, @PathVariable int majorPlanId){
//        User userFromSession = getUserFromSession(session);
//        List<Lesson> list = queryPlanByConditions("lesson_group", group, majorPlanId);
//        List<StudentLessonScore> studentLessonScores = userService.queryData(userFromSession);
//        List<Lesson> finalList = list;
//        list = list.stream().filter(new Predicate<Lesson>() {
//            @Override
//            public boolean test(Lesson lesson) {
//                //如果student里有，那么就去掉
//                if(lesson.getLessonId().equals("FL006001"))
//                    for(StudentLessonScore score:studentLessonScores)
//                        if(score.getLessonId().equals("FL006001"))
//                            return false;
//                if(lesson.getLessonId().equals("FL006002"))
//                    for(StudentLessonScore score:studentLessonScores)
//                        if(score.getLessonId().equals("FL006002"))
//                            return false;
//                if(lesson.getLessonId().equals("FL006003"))
//                    for(StudentLessonScore score:studentLessonScores)
//                        if(score.getLessonId().equals("FL006003"))
//                            return false;
//                if(lesson.getLessonId().equals("FL006004"))
//                    for(StudentLessonScore score:studentLessonScores)
//                        if(score.getLessonId().equals("FL006004"))
//                            return false;
//                if(lesson.getLessonId().equals("FL006005"))
//                    for(StudentLessonScore score:studentLessonScores)
//                        if(score.getLessonId().equals("FL006005"))
//                            return false;
//                return true;
//            }
//        }).collect(Collectors.toList());
//        List<StudentLessonScore> collect=null;
//        if(group.equals("all"))group="";
//        String finalGroup = group;
//        collect = studentLessonScores.stream().filter(studentLessonScore -> studentLessonScore.getLessonGroup().contains(finalGroup)).collect(Collectors.toList());
//        List<StudentLessonScore> score = transLesson2StudentLessonScore(list);
//        List<StudentLessonScore> finalCollect = collect;
//        List<StudentLessonScore> res = score.stream().filter(studentLessonScore -> !finalCollect.contains(studentLessonScore)).collect(Collectors.toList());
//        res = extracted(res);
//        List<Lesson> result = new ArrayList<>();
//        res.stream().forEach(studentLessonScore -> result.add(new Lesson(){{
//            setSemester(studentLessonScore.getSemester());
//            setLessonId(studentLessonScore.getLessonId());
//            setGradeScore(studentLessonScore.getGradeScore());
//            setLessonType(studentLessonScore.getLessonType());
//            setLessonName(studentLessonScore.getLessonName());
//            setLessonGroupName(studentLessonScore.getLessonGroup());
//        }}));
//        for(Lesson l:result){
//            for(Lesson lesson:list){
//                if(lesson.getLessonId().equals(l.getLessonId())){
//                    l.setCheckType(lesson.getCheckType());
//                    break;
//                }
//            }
//        }
//        result.sort((o1, o2) -> {
//            String[] split1 = o1.getSemester().split("-");
//            String[] split2 = o2.getSemester().split("-");
//            if(split1.length==1 || split1.length==0){
//                return 1;
//            }else if(split2.length==1 || split2.length==0){
//                return -1;
//            }
//            if(split2[0].equals(split1[0]) && split2[1].equals(split1[1])){
//                return Integer.valueOf(split1[2])-Integer.valueOf(split2[2]);
//            }else
//                return Integer.valueOf(split1[0])-Integer.valueOf(split2[0]);
//        });
//        return result;
//    }
    private List<Lesson> queryPlanByConditions(String attr,String input,int majorPlanId){
        Plan plan = planService.queryPlanById(majorPlanId);
        plan = fileUtils.readPlanData(plan);
        if(input.trim().equals("all")){
            return plan.getLessonList();
        }
        List<Lesson> res = new ArrayList<>();
        for (Lesson lesson:
                plan.getLessonList()
        ) {
            if(attr.equals("lesson_type")){
                if(lesson.getLessonType().contains(input)){
                    res.add(lesson);
                }
            }
            if(attr.equals("lesson_group")){
                if(lesson.getLessonGroupName().contains(input)){
                    res.add(lesson);
                }
            }
        }
        return res;
    }
    @ApiOperation(value = "更新成绩", notes = "可以在成绩查询页面中加一个按钮用来更新成绩，为减小服务器压力可以让用户点完一次更新后禁用这个按钮几分钟，没有参数是因为这是从session中获得密码和账号的，所以当没有成功登录的时候这个接口会报错", httpMethod = "POST")
    @PostMapping(value = "/updateData", produces = "application/json;charset=utf-8")
    public List<StudentLessonScore> updateData(@ApiIgnore HttpSession session) {
        List<StudentLessonScore> score = null;
        if(session.getAttribute("userLoginInfo")!=null){
            User user = getUserFromSession(session);
            user = userService.queryUserId(user.getUserId());
            score = userService.getScore(user);
            userService.updateUser(user,score);
        }else{
            throw new APIException(AppCode.APP_ERROR,"登录异常");
        }
        return score;
    }
    private List<StudentLessonScore> transLesson2StudentLessonScore(List<Lesson> lessons){
        String s = JSONArray.toJSONString(lessons);
        List<StudentLessonScore> studentLessonScores = JSONArray.parseArray(s, StudentLessonScore.class);
        for(StudentLessonScore score:studentLessonScores){
            for(Lesson l:lessons){
                if(l.getLessonId().equals(score.getLessonId()) && (l.getSemester()==null||l.getSemester().equals(score.getSemester()))){
                    score.setLessonGroupName(l.getLessonGroupName());
                }
            }
        }
        int len= studentLessonScores.size();
        for (int i = 0; i < len; i++) {
            StudentLessonScore score = studentLessonScores.get(i);
            if(score.getSemester()==null){
                score.setSemester("");
            }
            String[] semesters = score.getSemester().split(",");
            if(semesters.length!=1){
                for (int j = 0; j < semesters.length; j++) {
                    StudentLessonScore scoreNew = new StudentLessonScore(score);
                    scoreNew.setSemester(semesters[j]);
                    studentLessonScores.add(scoreNew);
                }
                studentLessonScores.remove(i);
                i--;
            }
        }
        return studentLessonScores;
    }
    @ApiOperation(value = "与培养方案进行匹配，看修了多少学分", notes = "按照传入的培养方案major_id进行对比，看每门课程修了多少学分，登陆后调用，在session中获取用户名和密码，返回的是按学期排序的课程和成绩，比如说我是软件工程的，我通过PlanController中的查找功能找到了自己的专业，前端获取了后端返回的专业的plan_id为351，那么接口只用填major_id为351，后端会把个人的成绩与培养方案做对比，返回培养方案中所有课程，对于每个课程，如果这个课程修过有分，会显示是否及格if_qualified与学生该门课的成绩student_score", httpMethod = "GET")
    @GetMapping(value = "/matchData/{major_id}", produces = "application/json;charset=utf-8")
    public List<StudentLessonScore> matchData(@ApiIgnore HttpSession session,@PathVariable int major_id) {
        //1.筛选出培养方案学期数目大于1的科目
        //2.用户数据中如果含有该课程或者多个该课程,把学期不存在成绩的除掉
        //3.不含该课程,选取最后一个学期的保留
        //4.特判情况:大学体育1-8 对应第1-8个学期 HE006007-HE006014
        //5.学业指导
        User userFromSession = getUserFromSession(session);
        List<StudentLessonScore> studentScore = userService.queryData(userFromSession);
        Plan plan = planService.queryPlanById(major_id);
        plan = planService.queryPlanData(plan);
        List<StudentLessonScore> planLesson = transLesson2StudentLessonScore(plan.getLessonList());
        extracted(studentScore, planLesson);
        planLesson.sort((o1, o2) -> {
            String[] split1 = o1.getSemester().split("-");
            String[] split2 = o2.getSemester().split("-");
            if(split1.length==1 || split1.length==0){
                return 1;
            }else if(split2.length==1 || split2.length==0){
                return -1;
            }
            if(split2[0].equals(split1[0]) && split2[1].equals(split1[1])){
                return Integer.valueOf(split1[2])-Integer.valueOf(split2[2]);
            }else
                return Integer.valueOf(split1[0])-Integer.valueOf(split2[0]);
        });
        return planLesson;
    }

    private static List<StudentLessonScore> extracted(List<StudentLessonScore> planLesson) {
        Map<String, List<StudentLessonScore>> stringListMap = extracted1(planLesson);
        List<StudentLessonScore> list = new ArrayList<>();
        for (Map.Entry<String, List<StudentLessonScore>> e:
        stringListMap.entrySet()){
            e.getValue().sort((o1, o2) -> {
                String[] split1 = o1.getSemester().split("-");
                String[] split2 = o2.getSemester().split("-");
                if(split1.length==1 || split1.length==0){
                    return 1;
                }else if(split2.length==1 || split2.length==0){
                    return -1;
                }
                if(split2[0].equals(split1[0]) && split2[1].equals(split1[1])){
                    return Integer.valueOf(split1[2])-Integer.valueOf(split2[2]);
                }else
                    return Integer.valueOf(split1[0])-Integer.valueOf(split2[0]);
            });
            list.addAll( e.getValue().stream().skip(e.getValue().size() - 1).collect(Collectors.toList()));
        }
        return list;
    }

    private static Map<String, List<StudentLessonScore>> extracted1(List<StudentLessonScore> planLesson) {
        Map<String,List<StudentLessonScore>> map = new HashMap<>();
        for(StudentLessonScore l : planLesson){
            if(map.containsKey(l.getLessonId())){
                map.get(l.getLessonId()).add(l);
            }else{
                map.put(l.getLessonId(),new ArrayList<StudentLessonScore>(){{add(l);}});
            }
        }
        return map;
    }

    private static void extracted(List<StudentLessonScore> studentScore, List<StudentLessonScore> planLesson) {
        Map<String, List<StudentLessonScore>> map = extracted1(planLesson);

        for (int i = 0; i < studentScore.size(); i++) {
            StudentLessonScore lesson = studentScore.get(i);
            if(planLesson.contains(lesson)){
                map.get(lesson.getLessonId()).remove(lesson);
                if(map.get(lesson.getLessonId()).size()==0)map.remove(lesson.getLessonId());
                StudentLessonScore studentLessonScore = planLesson.get(planLesson.indexOf(lesson));
                studentLessonScore.setStudentScore(lesson.getStudentScore());
                studentLessonScore.setIfQualified(lesson.getIfQualified());
            }else{
                planLesson.add(lesson);
            }
        }
        for(Map.Entry<String,List<StudentLessonScore>> e:map.entrySet()){
            boolean exist = false;
            for(StudentLessonScore s: studentScore){
                if(s.getLessonId().equals(e.getValue().get(0).getLessonId())){
                    exist=true;
                }
            }
            if(exist==false)
                e.getValue().remove(e.getValue().size()-1);
            planLesson.removeAll(e.getValue());
        }
    }

    public List<Lesson> specialJudge(List<Lesson> planLesson) {
        int i=0;
        for(Lesson l:planLesson){
            if(l.getLessonName().startsWith("大学体育")){
                String[] split = l.getSemester().split(",");
                if(split.length==1)l.setSemester(split[0]);
                else
                    l.setSemester(split[i]);
                if(split.length>1)i++;
            }
        }
        return planLesson;
    }

    public List<StudentLessonScore> getData4User(User user) {
        boolean hasUser = userService.hasUser(user);
        user.setScoreData(user.getUserId()+".json");
        List<StudentLessonScore> score = null;
        if (!hasUser) {
            score = userService.getScore(user);
            if (score == null) {
                throw new APIException(AppCode.APP_ERROR,"登录异常");
            } else {
                for(StudentLessonScore l:score){
                    if(l.getLessonId().equals("HE006001")){
                        l.setLessonId("HE006007");
                        l.setLessonName("大学体育(Ⅰ)");
                    }
                    else if(l.getLessonId().equals("HE006002")){
                        l.setLessonId("HE006008");
                        l.setLessonName("大学体育(Ⅱ)");
                    }
                    else if(l.getLessonId().equals("HE006003")){
                        l.setLessonId("HE006009");
                        l.setLessonName("大学体育(Ⅲ)");
                    }
                    else if(l.getLessonId().equals("HE006004")){
                        l.setLessonId("HE006010");
                        l.setLessonName("大学体育(Ⅳ)");
                    }
                    else if(l.getLessonId().equals("HE006005")){
                        l.setLessonId("HE006011");
                        l.setLessonName("大学体育(Ⅴ)");
                    }
                    else if(l.getLessonId().equals("HE006006")){
                        l.setLessonId("HE006012");
                        l.setLessonName("大学体育(Ⅵ)");
                    }
                }
                if(userService.hasUser(user)){
                    userService.updateUser(user,score);
                }else{
                    userService.addUser(user);
                    fileUtils.writeUserFile(user,score);
                }
            }
        } else {
            score = userService.queryData(user);
            if(score==null){
                throw new APIException(AppCode.APP_ERROR,"登录异常");
            }
        }
        return score;
    }
    private User getUserFromSession(HttpSession session){
        Object loginInfo = session.getAttribute("userLoginInfo");
        if(loginInfo==null)throw new APIException(AppCode.APP_ERROR,"用户未登录");
        String userLoginInfo = loginInfo.toString().substring(37);
        String user_id = userLoginInfo.substring(0, userLoginInfo.indexOf("_"));
        String password = userLoginInfo.substring(userLoginInfo.indexOf("_")+1);
        User user = new User();
        user.setUserId(user_id);
        user.setPassword(password);
        return user;
    }

}
