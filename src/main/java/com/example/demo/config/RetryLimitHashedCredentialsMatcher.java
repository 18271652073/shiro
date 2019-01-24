package com.example.demo.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @date 2018/9/5.
 */

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);
    @Autowired
    private RedisClient redisClient;
//    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
//    private Cache<String, AtomicInteger> passwordRetryCache;
//
//    public RetryLimitHashedCredentialsMatcher(RedisCacheManager redisCacheManager) {
//        passwordRetryCache = redisCacheManager.getCache("passwordRetryCache");
//
//    }

//    @Override
//    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
//        String username = (String) token.getPrincipal();
//        AtomicInteger retryCount = passwordRetryCache.get(username);
//        if (null == retryCount) {
//            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(username, retryCount);
//        }
//        if (retryCount.incrementAndGet() > 3) {
//            logger.warn("username: " + username + " tried to login more than 3 times in period");
//            throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 3 times in period");
//        }
//        boolean matches = super.doCredentialsMatch(token, info);
//        if (matches) {
//            //clear retry data
//            passwordRetryCache.remove(username);
//        }
//        return matches;
//    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        Integer count = (int) (redisClient.get(username) == null ? 0 : redisClient.get(username));
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry data
            redisClient.remove(username);
        } else {
            if (count > 1) {
                logger.warn("username: " + username + " tried to login more than 2times in period");
                throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 2 times in period");
            }
            redisClient.set(username, count + 1, (long) 300);
            System.out.println("登录失败次数！" + count + 1);
        }
        return matches;
    }
}
