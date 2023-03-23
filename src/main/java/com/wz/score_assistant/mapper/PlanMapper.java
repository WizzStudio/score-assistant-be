package com.wz.score_assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wz.score_assistant.entity.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlanMapper extends BaseMapper<Plan> {
    List<Plan> queryPlanByGrade(int grade);

    List<Plan> queryGradePlanNameAmbiguously(int grade, String input);

    List<Plan> queryAll();
}
