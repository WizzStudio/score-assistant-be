package com.wz.score_assistant.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户实体类")
@TableName("users")
public class User implements Serializable {
    @TableId
    @ApiModelProperty("学号")
    private String userId;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("成绩数据")
    private String scoreData;

}

