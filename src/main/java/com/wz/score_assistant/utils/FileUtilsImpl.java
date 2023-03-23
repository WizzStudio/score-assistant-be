package com.wz.score_assistant.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wz.score_assistant.entity.Lesson;
import com.wz.score_assistant.entity.Plan;
import com.wz.score_assistant.entity.User;
import com.wz.score_assistant.exception.APIException;
import com.wz.score_assistant.mapper.LessonMapper;
import com.wz.score_assistant.mapper.PlanMapper;
import com.wz.score_assistant.vo.AppCode;
import com.wz.score_assistant.vo.StudentLessonScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;


@Component
@Slf4j
public class FileUtilsImpl implements FileUtils {
    @Value("${user-data-path}")
    public String USER_DATA_PATH;
    @Resource
    private PlanMapper planDao;
    @Resource
    private LessonMapper lessonDao;
    public String SOURCE_PATH = "source_data/";
    public String CLEAN_PATH = "clean_data/";
    public String GROUP_All_PATH ="group_clean_data/all_group.json";
    public String GROUP_PATH = "group_clean_data/";
    public String USER_PATH = "user_data/";

    @Override
    public String readFile(String filePath) {
        InputStream stream = null;
        try {
        ClassPathResource resource = new ClassPathResource(filePath);
         stream = resource.getStream();

            String read = IoUtil.read(stream, Charset.forName("utf-8"));
            return read;
        }catch (RuntimeException e){
            throw new APIException(AppCode.APP_ERROR,"数据不存在");
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new APIException(AppCode.APP_ERROR,"io异常");
            }catch (NullPointerException e){
                throw new APIException(AppCode.APP_ERROR,"文件不存在");
            }
        }
    }

    @Override
    public void writeFile(String filePath, String data) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new APIException(AppCode.APP_WRITE_ERROR, "创建文件失败");
            }
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
    }

    @Override
    public JSONArray wrapDataReadFile(String filePath) {
        return JSONArray.parseArray(readFile(filePath));
    }

    @Override
    public void explainSourceDataAndCollect() {
        Set<Lesson> allLesson = new HashSet<>();
        Map<String, String> type = new HashMap<>();
        File file = new File(SOURCE_PATH);
        System.out.println(file.getAbsolutePath());
        File[] year = file.listFiles();
        for (File f : year) {
            File[] plans = f.listFiles();
            File cleanPlan = new File(CLEAN_PATH + f.getName() + '/');
            cleanPlan.mkdir();
            for (File plan : plans) {
                String path = f.getName() + '/' + plan.getName();
                Plan toAdd = new Plan(
                        Integer.parseInt(f.getName()),
                        path,
                        plan.getName().substring(0, plan.getName().lastIndexOf('.'))
                );

                File planFile = new File(CLEAN_PATH + path);
                try {
                    planFile.createNewFile();
                } catch (IOException e) {
                    System.out.println("error");
                }
                String s = readFile(SOURCE_PATH + path);
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray jsonArray = jsonObject.getJSONObject("datas")
                        .getJSONObject("kzkccx")
                        .getJSONArray("rows");
                toAdd.setPlanCode(jsonArray.getJSONObject(0).getString("PYFADM"));
//                planDao.insert(toAdd);
                JSONArray result = new JSONArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject planLesson = jsonArray.getJSONObject(i);
                    Lesson lesson = new Lesson(
                            planLesson.getString("KCH"),
                            planLesson.getString("XNXQ"),
                            planLesson.getString("KCM"),
                            planLesson.getString("KCXZDM_DISPLAY"),
                            planLesson.getString("KZM"),
                            planLesson.getString("KZH"),
                            planLesson.getString("XF"),
                            planLesson.getString("KSLXDM_DISPLAY")
                    );
                    type.put(planLesson.getString("KCXZDM"), planLesson.getString("KCXZDM_DISPLAY"));
                    if (lesson.getSemester() != null) {
                        lesson.setSemester(lesson.getSemester().replaceAll("，", ","));
                        lesson.setSemester(lesson.getSemester().replaceAll("学年第二学期", "-2"));
                        lesson.setSemester(lesson.getSemester().replaceAll("学年第一学期", "-1"));
                    }
                    allLesson.add(lesson);
                    result.add(lesson);
                }
                JSONObject res = new JSONObject();
                res.put("plan_code", jsonArray.getJSONObject(0).getString("PYFADM"));
                res.put("row", result);
                writeFile(planFile.getPath(), res.toJSONString());
                System.out.println(planFile.getAbsolutePath());
//                System.out.println("=================");
//                System.out.println(result.toJSONString());
            }
        }
        for (Lesson l : allLesson) {
//            lessonDao.insert(l);
        }
        for (Map.Entry<String, String> entry :
                type.entrySet()) {
            System.out.println(entry.getKey() + "====>" + entry.getValue());
        }
        System.out.println(allLesson.size());
    }

    @Override
    public List<StudentLessonScore> getUserScore(String path) {
        String jsonStr = readUserFile(USER_DATA_PATH + path);
        return JSONArray.parseArray(jsonStr, StudentLessonScore.class);
    }

    private String readUserFile(String path) {
        FileReader fileReader = new FileReader(path);
        return fileReader.readString();
    }


    @Override
    public String explainGroupSourceDataAndBuildTree() {
        String s = readFile(GROUP_All_PATH);
        JSONArray jsonArray = JSONArray.parseArray(s);
        HashMap<String,JSONArray> map = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String pyfadm = jsonObject.getString("PYFADM");
            if(map.containsKey(pyfadm)){
                map.get(pyfadm).add(jsonObject);
            }else{
                map.put(pyfadm,new JSONArray(){{
                    add(jsonObject);
                }});
            }
        }
        List<Plan> plans = planDao.queryAll();
        System.out.println(plans.size());
        for(Plan p : plans){
            System.out.println(p.getGrade()+"++"+p.getPlanName()+"==>"+ map.get(p.getPlanCode()).size());
            String path = p.getGrade()+"/"+p.getPlanName()+".json";
            JSONArray treeSource = map.get(p.getPlanCode());
            JSONObject root = new JSONObject();
            root.put("name",p.getPlanName());
            root.put("code",p.getPlanCode());
            root.put("id",p.getPlanId());
            root.put("grade",p.getGrade());
            root.put("KZH","-1");
            System.out.println(buildTreeDFS(treeSource, root).toJSONString());
            writeFile(GROUP_PATH+path,buildTreeDFS(treeSource,root).toJSONString());
        }
        return null;
    }
    public JSONObject buildTreeDFS(JSONArray treeSource,JSONObject root){
        JSONArray tempJson = new JSONArray();
        for (int i = 0; i < treeSource.size(); i++) {
            if(treeSource.getJSONObject(i).getString("FKZH").equals(root.getString("KZH"))){
                tempJson.add(treeSource.getJSONObject(i));
            }
        }
        treeSource.removeAll(tempJson);
        for (int i = 0; i < tempJson.size(); i++) {
            buildTreeDFS(treeSource,tempJson.getJSONObject(i));
        }
        if(tempJson.size()!=0){
            root.put("children",tempJson);
        }
        return root;
    }
    @Override
    public Plan readPlanData(Plan plan) {
        String s = readFile(CLEAN_PATH + plan.getPlanData());
        JSONObject object = JSONObject.parseObject(s);
        plan.setLessonList(JSONArray.parseArray(object.getString("row"),Lesson.class));
        plan.setPlanCode(object.getString("plan_code"));
        return plan;
    }

    @Override
    public User writeUserFile(User user, List<StudentLessonScore> score) {
        writeFile(USER_DATA_PATH + user.getScoreData(), JSONArray.toJSONString(score));
        return user;
    }

    @Override
    public JSONObject readTreeData(String path) {
        String treeJson = readFile(GROUP_PATH+path);
        return JSONObject.parseObject(treeJson);
    }
}
