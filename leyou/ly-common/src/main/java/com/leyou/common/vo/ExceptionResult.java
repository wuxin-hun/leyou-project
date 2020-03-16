package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/27 1:23
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum em) {
         this.status=em.getCode();
         this.message=em.getMsg();
          this.timestamp=System.currentTimeMillis();
    }
}
