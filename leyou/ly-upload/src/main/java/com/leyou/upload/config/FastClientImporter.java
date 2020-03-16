package com.leyou.upload.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;


/**
 * 这个是FastDFS的客户端的纯java配置文件，可以帮我们直接注入客户端，视频上直接copy过来的。不需要管。
 * 再加上一个编写DFS的配置文件在application.yml中配置地址，缩率图大小。
 */
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)

public class FastClientImporter {
}