package com.wz.score_assistant.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wz.score_assistant.vo.StudentLessonScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class DataUtilsImpl implements DataUtils{

    @Override
    public List<StudentLessonScore> explainPyData(String str) {
        JSONArray jsonArray = JSONArray.parseArray(str);
        List<StudentLessonScore> scores = new ArrayList<>();
        for(int i =0;i<jsonArray.size();i++){
            JSONObject score = jsonArray.getJSONObject(i);
            StudentLessonScore lesson = new StudentLessonScore(
                    score.getString("KCH"),
                    score.getString("KSSJ"),
                    score.getString("XNXQDM"),
                    score.getString("KCXZDM_DISPLAY"),
                    score.getString("KCLBDM_DISPLAY"),
                    score.getString("XSKCM"),
                    score.getString("ZCJ"),
                    score.getString("XF"),
                    score.getString("SFJG"),
                    score.getString("KSLXDM_DISPLAY")

            );
            scores.add(lesson);
        }
        return scores;
    }
}
