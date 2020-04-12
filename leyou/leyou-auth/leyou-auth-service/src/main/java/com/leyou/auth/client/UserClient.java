package com.leyou.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 这个地方按照视频上应该是在use的微服务里面有一个接口，然后这边继承他的接口，内容是空的就可以了。
 * @Author TT Hun
 * @Data 2020/4/4 21:45
 */
@FeignClient("user-service")
public interface UserClient extends UserApi{

    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username, @RequestParam("password")String password));
}
