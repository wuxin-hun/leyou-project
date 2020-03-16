package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/27 1:09
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
//通过向get方法里面传入一个枚举类，来构造这个RunttimeException
public class LyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;



}
