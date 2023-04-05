package com.wz.score_assistant.aop;

import com.wz.score_assistant.exception.APIException;
import com.wz.score_assistant.vo.ResultCode;
import com.wz.score_assistant.vo.ResultVo;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.wz.score_assistant"})
public class ControllerExceptionAdvice {
    @ExceptionHandler({BindException.class})
    public ResultVo MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResultVo(ResultCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }
    @ExceptionHandler(APIException.class)
    public ResultVo APIExceptionHandler(APIException e) {
        // log.error(e.getMessage(), e); 由于还没集成日志框架，暂且放着，写上TODO
        return new ResultVo(e.getCode(), e.getMsg(), e.getMessage());
    }
}
