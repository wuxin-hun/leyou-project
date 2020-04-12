package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.netflix.discovery.converters.Auto;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/4/4 21:23
 */
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
//        根据用户名和密码查询，需要远程调用通过feign
        User user = userClient.queryUser(username, password);
//        判断user
        if(user==null){
            return null;
        }
        try {
//        不为空通过jwtUtils生成jwt类型的token
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getuserName());

          return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
//
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
