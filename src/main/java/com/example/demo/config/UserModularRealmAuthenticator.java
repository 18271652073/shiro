package com.example.demo.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Administrator
 * @date 2018/8/30.
 */
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken){
        Collection<Realm> realms = getRealms();
        Collection<Realm> typeRealms = new ArrayList<>();
        UserToken userToken=(UserToken)authenticationToken;
        String loginType = userToken.getLoginType();
        if(loginType==null||loginType.equals("Admin")){
            loginType="AuthRealm";
        }
        System.out.println("UserModularRealmAuthenticator:装载 Realms");
        for (Realm realm : realms) {
            if(realm.getName().contains(loginType)){
                typeRealms.add(realm);
            }
        }
        return typeRealms.size() == 1?this.doSingleRealmAuthentication(typeRealms.iterator().next(), userToken):this.doMultiRealmAuthentication(typeRealms, userToken);
    }
}
