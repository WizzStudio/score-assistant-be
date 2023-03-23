package com.wz.score_assistant.service;


import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.mapper.PlanMapper;
import com.wz.score_assistant.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanService {
    @Resource
    private PlanMapper planDao;
    @Resource
    FileUtils fileUtils;
    public int addPlan(Plan plan){
        return planDao.insert(plan);
    }
    public List<Plan> queryPlanByGrade(int grade){
        return planDao.queryPlanByGrade(grade);
    }
    public List<Plan> queryAll(){
        return planDao.queryAll();
    }
    public List<Plan> queryGradePlanNameAmbiguously(int grade,String input){
        return planDao.queryGradePlanNameAmbiguously(grade,input);
    }
    public Plan queryPlanById(int plan_id){
        return planDao.selectById(plan_id);
    }
    public Plan queryPlanData(Plan plan){
        return fileUtils.readPlanData(plan);
    }

}
