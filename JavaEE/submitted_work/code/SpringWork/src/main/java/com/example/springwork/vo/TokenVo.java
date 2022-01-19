package com.example.springwork.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.example.springwork.entity.User;

/**
 * @ClassName: TokenVo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/16 0:55
 * @Version: v1.0
 */
public class TokenVo {
    private User user;

    private SaTokenInfo token;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public SaTokenInfo getToken() {
        return token;
    }

    public void setToken(SaTokenInfo token) {
        this.token = token;
    }
}
