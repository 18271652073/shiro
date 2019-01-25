package com.example.demo.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Administrator
 * @date 2018/8/30.
 */
public class UserToken extends UsernamePasswordToken {
    //登录类型，判断是学生登录，教师登录还是管理员登录
    private String loginType;
    private String kickOut;

    public UserToken(final String username, final String password, String loginType, String kickOut) {
        super(username, password);
        this.loginType = loginType;
        this.kickOut = kickOut;
    }

    public UserToken(final String username, final String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public UserToken(final String username, final String password) {
        super(username, password);
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getKickOut() {
        return kickOut;
    }

    public void setKickOut(String kickOut) {
        this.kickOut = kickOut;
    }
}
