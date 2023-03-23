package com.wz.score_assistant.vo;

import lombok.Getter;

@Getter
public enum  AppCode implements StatusCode {

    APP_ERROR(2000, "业务异常"),
    APP_WRITE_ERROR(2001, "读写异常");

    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}