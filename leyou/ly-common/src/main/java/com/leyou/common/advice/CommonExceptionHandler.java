package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/27 0:53
 */
@ControllerAdvice//默认情况下拦截所有的Controller的异常，一旦出现一场了使用这个处理
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)//选择拦截哪种异常。
//   出现了这种异常就会将这种异常的实例传入函数当中，这个异常里面包括了
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        ExceptionEnum em = e.getExceptionEnum();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }



}
