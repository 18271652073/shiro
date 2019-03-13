package com.example.demo.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Administrator
 * @date 2018/8/24.
 */
@Configuration
public class ShiroConfig {


    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher=new RetryLimitHashedCredentialsMatcher(cacheManager());
        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher=new RetryLimitHashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    @Bean(name = "authRealm")
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return authRealm;
    }
    @Bean(name = "secondRealm")
    public SecondRealm secondRealm() {
        SecondRealm secondRealm = new SecondRealm();
        return secondRealm;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        redisManager.setExpire(300);// 配置缓存过期时间5分钟
        redisManager.setTimeout(0);
        // redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
//        redisSessionDAO.setKeyPrefix("shiroSessionCache");
        return redisSessionDAO;
    }
    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        System.out.println("sessionManager...");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager() {
        System.out.println("securityManager...");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        Collection<Realm> typeRealms = new ArrayList<>();
        typeRealms.add(authRealm());
        typeRealms.add(secondRealm());
        securityManager.setAuthenticator(new UserModularRealmAuthenticator());
        securityManager.setRealms(typeRealms);
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) throws IOException {
        System.out.println("shiroFilter...");
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //oauth过滤
        Map<String,Filter> filters = new HashMap<>();
        filters.put("oauth2", new MyAccessControlFilter());
//        filters.put("corsFilter", new CORSAuthenticationFilter());
        shiroFilter.setFilters(filters);
//        Map<String, String> filterMap = new LinkedHashMap<>();
        Map<String, String> filterMap = initUrl();

        filterMap.put("/access/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");

        filterMap.put("/login/loginPage", "anon");
        filterMap.put("/login/error", "anon");
        filterMap.put("/login/check", "anon");
        filterMap.put("/login/register", "anon");
        filterMap.put("/login/loginPage", "anon");
        filterMap.put("/login/loginOut", "anon");
        filterMap.put("/login/captcha.img", "anon");
        filterMap.put("/testLogin/loginPage", "anon");
        filterMap.put("/testLogin/error", "anon");
        filterMap.put("/testLogin/check", "anon");
        filterMap.put("/testLogin/register", "anon");
        filterMap.put("/testLogin/loginPage", "anon");
        filterMap.put("/testLogin/loginOut", "anon");
        filterMap.put("/testLogin/captcha.img", "anon");
        filterMap.put("/comment/lock", "anon");
//        filterMap.put("/home/first", "oauth2[power:1]");

        filterMap.put("/js/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/**", "oauth2");
//        filterMap.put("/**", "corsFilter");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }
    /**
     * 初始化url路径
     */
    private Map<String, String> initUrl() throws IOException {
        System.out.println("初始化url");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("url.properties");
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String str = null;
        try {
            while ((str = br.readLine()) != null) {
                if (StringUtils.isEmpty(str) || str.contains("#")) {
                    continue;
                }
                String[] arr = str.split("=");
                filterChainDefinitionMap.put(arr[0].trim(), arr[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return filterChainDefinitionMap;
    }

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        ModularRealmAuthenticator modularRealmAuthenticator=new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        System.out.println("lifecycleBeanPostProcessor...");
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        System.out.println("defaultAdvisorAutoProxyCreator...");
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        System.out.println("authorizationAttributeSourceAdvisor...");
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

}
