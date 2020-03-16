package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/12/7 18:12
 */
@Data//lombok注解生成getset方法
@Component//下面那个ConfigurationProperties拿到配置文件中的配置的属性，单写上会报错，加上Component添加到容器当中就不报错了原因为止
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;

}


