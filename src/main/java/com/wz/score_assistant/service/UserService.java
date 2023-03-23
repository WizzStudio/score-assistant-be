package com.wz.score_assistant.service;

import cn.hutool.core.codec.Base64Decoder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wz.score_assistant.entity.User;
import com.wz.score_assistant.exception.APIException;
import com.wz.score_assistant.mapper.UserMapper;
import com.wz.score_assistant.utils.DataUtils;
import com.wz.score_assistant.utils.FileUtils;
import com.wz.score_assistant.vo.AppCode;
import com.wz.score_assistant.vo.StudentLessonScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserService {
    @Resource
    private DataUtils dataUtils;
    @Resource
    private FileUtils fileUtils;
    @Resource
    private UserMapper userDao;
    @Value("${get-score-cmd}")
    String getScoreCMD;
    public int addUser(User user) {
        return userDao.insert(user);
    }

    public int deleteUser(String user_id) {
        return userDao.deleteById(user_id);
    }

    public int updateUser(User user,List<StudentLessonScore> score) {
        fileUtils.writeUserFile(user,score);
        return userDao.updateById(user);
    }

    public List<StudentLessonScore> queryData(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,user.getUserId()).and(qw->qw.eq(User::getPassword,user.getPassword()));
        User one = userDao.selectOne(queryWrapper);
        if(one==null)return null;
        String path = one.getScoreData();
        List<StudentLessonScore> userScore = fileUtils.getUserScore(path);
        return userScore;
    }

//    public User queryUser(String user_id, String password) {
//        return userDao.queryUser(user_id, password);
//    }

    public User queryUserId(String user_id) {
        return userDao.selectById(user_id);
    }
    public List<StudentLessonScore> getScore(User user){
        String user_id = user.getUserId();
        String password = Base64Decoder.decodeStr(user.getPassword());
        String res="";
        Process proc=null;
        try {
            URL resource = this.getClass().getClassLoader().getResource("get_grades.py");
            String[] cmds = new String[4];
            cmds[0] = getScoreCMD.split(" ")[0];
            cmds[1] = getScoreCMD.split(" ")[1];
            cmds[2] = user_id;
            cmds[3] = password;
            proc = Runtime.getRuntime().exec(cmds);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName("utf-8")));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(proc.getErrorStream(), Charset.forName("utf-8")));
            String line="";
            while ((line = bufferedReader.readLine()) != null)
                res += line;
            bufferedReader.close();
            errReader.close();
            proc.waitFor(5, TimeUnit.SECONDS);
        }catch (Exception e) {
            throw new APIException(AppCode.APP_ERROR,"接口异常");
        }finally {
            if(proc!=null)
            proc.destroyForcibly();
            else
                throw new APIException(AppCode.APP_ERROR,"查询异常");
        }
        if(res=="")
            throw new APIException(AppCode.APP_ERROR,"未查询到数据");
        return dataUtils.explainPyData(res);
    }

    public boolean hasUser(User u) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,u.getUserId());
        return userDao.exists(queryWrapper);
    }
}
