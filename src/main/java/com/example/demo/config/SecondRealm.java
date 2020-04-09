package com.example.demo.config;

import com.example.demo.dom.all.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * @date 2018/8/30.
 */
public class SecondRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("SecondRealm权限验证");
        User user = (User) principalCollection.getPrimaryPrincipal();
        Integer userId = user.getUid();
        Set<String> setList = userService.findPower(userId.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> rolesSet = new HashSet<>();
        rolesSet.add("customer");
        info.setRoles(rolesSet);
        info.setStringPermissions(setList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("SecondRealm身份验证");
        UserToken token = (UserToken) authenticationToken;
        if (token.getLoginType() == null) {
            try {
                throw new Exception("SecondRealm:yonghu leixing null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String username = token.getUsername();
        User user = userService.findUser(username);
        if (user == null) {
            try {
                throw new Exception("用户信息不存在数据库！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
