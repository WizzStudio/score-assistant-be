package com.wz.score_assistant.dao;

import com.wz.score_assistant.entity.User;
import org.apache.ibatis.annotations.*;


public interface UserDao {
    @Insert("insert into wz_sc.users(user_id, password, score_data)\n" +
            "        values (#{user_id},#{password},#{score_data})")
    int addUser(User user);
    @Delete("delete from wz_sc.users where user_id = #{user_id};")
    int deleteUser(String user_id);
    @Update("update wz_sc.users\n" +
            "        set user_id = #{user_id},password = #{password},score_data=#{score_data}\n" +
            "        where user_id = #{user_id}")
    int updateUser(User user);
    @Select("select score_data from wz_sc.users where user_id = #{user_id} and password = #{password}")
    String queryData(User user);
    @Select("select ifnull(user_id,-1) as user_id,password, score_data from wz_sc.users where user_id=#{user_id} and password =#{password}")
    User queryUser(@Param("user_id") String user_id,@Param("password") String password);
    @Select("select ifnull(user_id,-1) as user_id,password, score_data from wz_sc.users where user_id=#{user_id}")
    User queryUserId(String user_id);
    @Select("select ifnull(user_id,-1) as user_id,password, score_data from wz_sc.users where user_id=#{user_id}")
    User queryHasUser(String user_id);
}
