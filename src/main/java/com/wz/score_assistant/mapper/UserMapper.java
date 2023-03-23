package com.wz.score_assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wz.score_assistant.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
