package com.example.demo.config;

import com.example.demo.dom.all.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * @date 2018/8/24.
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisSessionDAO redisSessionDAO;
//    @Autowired
//    private RedisClient redisClient;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("AuthRealm权限验证");
        User user = (User) principalCollection.getPrimaryPrincipal();
        Integer userId = user.getUid();
        Set<String> setList = userService.findPower(userId.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> rolesSet = new HashSet<>();
//        rolesSet.add("customer");
        rolesSet.add("admin");
        info.setRoles(rolesSet);
        info.setStringPermissions(setList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        System.out.println("AuthRealm身份验证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.findUser(username);
        if (user == null) {
            throw new IncorrectCredentialsException("AuthRealm:用户身份: null");
        }
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for (Session session : sessions) {
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null && (user.toString()).equals(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY).toString())) {
                throw new ConcurrentAccessException("早已登陆！");
            }
        }
        //return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getUsername() + user.getSalt()),//salt=username+salt,采用明文访问时，不需要此句
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
