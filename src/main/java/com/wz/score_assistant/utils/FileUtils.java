package com.wz.score_assistant.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.entity.User;
import com.wz.score_assistant.vo.StudentLessonScore;

import java.util.List;

public interface FileUtils {
    String readFile(String filePath);
    void writeFile(String filePath,String data);
    JSONArray wrapDataReadFile(String filePath);
    void explainSourceDataAndCollect();
    List<StudentLessonScore> getUserScore(String path);
    String explainGroupSourceDataAndBuildTree();
    Plan readPlanData(Plan plan);

    User writeUserFile(User user, List<StudentLessonScore> score);

    JSONObject readTreeData(String path);
}
