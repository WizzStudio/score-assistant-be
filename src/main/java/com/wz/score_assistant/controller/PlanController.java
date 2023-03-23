package com.wz.score_assistant.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wz.score_assistant.entity.Lesson;
import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.exception.APIException;
import com.wz.score_assistant.service.PlanService;
import com.wz.score_assistant.utils.FileUtils;
import com.wz.score_assistant.vo.AppCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "培养方案接口",description = "包括培养方案内信息的具体查询")
@RequestMapping("/plan")
@RestController
public class PlanController {
    @Resource
    PlanService planService;
    @Resource
    FileUtils fileUtils;
    @ApiOperation(value = "获取一个年级的所有培养方案，但不生成数据",notes = "",httpMethod = "GET")
    @GetMapping(value = "/planList/{grade}",produces = "application/json;charset=utf-8")
    public List<Plan> queryAllPlanIdAndName(@PathVariable int grade){
        List<Plan> plans = planService.queryPlanByGrade(grade);
        if(plans.size()==0){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        return plans;
    }
    static ArrayList<Integer> grades = new ArrayList(){{
        add(2019);
        add(2020);
        add(2021);
        add(2022);
    }};

    @ApiOperation(value = "获取年级",notes = "",httpMethod = "GET")
    @GetMapping(value = "/grades",produces = "application/json;charset=utf-8")
    public List<Integer> queryGrades(){
        return grades;
    }
    @ApiOperation(value = "获取培养方案id对应的树状课程json")
    @GetMapping(value = "/queryTree/{plan_id}")
    public JSONObject queryPlanLessonTreeById(@PathVariable int plan_id){
        Plan plan = planService.queryPlanById(plan_id);
        if(plan==null){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        String path = plan.getPlanData();
        JSONObject object = fileUtils.readTreeData(path);
        return object;
    }
    @ApiOperation(value = "按照年级获取名字与输入相似的培养方案",notes = "例如查找2022年的软件工程，grade为2022，input为软件工程的子串就行，会返回所有跟输入匹配的培养方案的plan_id和名称",httpMethod = "GET")
    @GetMapping(value = "/{grade}/namesLikeInput/{input}",produces = "application/json;charset=utf-8")
    public List<Plan> queryPlanNameLike(@PathVariable int grade,@PathVariable String input){
        List<Plan> likes = planService.queryGradePlanNameAmbiguously(grade,input);
//        for(Plan plan:likes){
//            plan = fileUtils.readPlanData(plan);
//        }
        if(likes.size()==0){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        return likes;
    }
    @ApiOperation(value = "按照培养方案的id获取里面所有课程类型含type字串的课程",notes = "当type参数为all时返回所有课程，例如你用上面的接口查出了2022软件工程的id是351，majorPlanId就填351，你要查找所有类型为必修的课程，那么type就填必修，type填的啥，返回的就是该majorPlanId下所有课程类型名字里有type子串的课程",httpMethod = "GET")
    @GetMapping(value = "/query/{majorPlanId}/{type}",produces = "application/json;charset=utf-8")
    public List<Lesson> queryLessonByType(@PathVariable int majorPlanId, @PathVariable String type){
        List<Lesson> lessons = queryPlanByConditions("lesson_type", type, majorPlanId);
        if(lessons.size()==0){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        return lessons;
    }
    @ApiOperation(value = "按照培养方案的id获取里面所有课程类型的课程",httpMethod = "POST")
    @PostMapping(value = "/queryTypes/{majorPlanId}",produces = "application/json;charset=utf-8")
    public List<Lesson> queryLessonByTypes(@PathVariable int majorPlanId,@RequestBody JSONObject types){
        List<String> strings =JSONArray.parseArray(types.get("types").toString(), String.class);
//        List<String> strings = types.toJavaList(String.class);
        List<Lesson> lessons = new ArrayList<>();
        for(String type : strings)
            lessons.addAll(queryPlanByConditions("lesson_type", type, majorPlanId));
        if(lessons.size()==0){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        return lessons;
    }
    @ApiOperation(value = "按照培养方案的id获取里面所有课程",httpMethod = "GET")
    @GetMapping(value = "/query/{majorPlanId}",produces = "application/json;charset=utf-8")
    public List<Lesson> queryLessonAll(@PathVariable int majorPlanId){
        List<Lesson> lessons = queryPlanByConditions("lesson_type", "all", majorPlanId);
        if(lessons.size()==0){
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        }
        return lessons;
    }
    @ApiOperation(value = "按照培养方案的id获取里面所有课程组名名中包含group的课程",notes = "当type参数为all时返回所有课程，例如你要查找所有课组名为专业选修课的课程，那么group就填专业选修课",httpMethod = "GET")
    @GetMapping(value = "/queryL/{majorPlanId}/{group}",produces = "application/json;charset=utf-8")
    public List<Lesson> queryLessonByGroup(@PathVariable String group, @PathVariable int majorPlanId){
        return queryPlanByConditions("lesson_group",group,majorPlanId);
    }
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
}
