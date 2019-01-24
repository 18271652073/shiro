package com.example.demo.service;

import com.example.demo.dom.all.entity.Captcha;
import com.example.demo.dom.all.entity.User;
import com.example.demo.dom.all.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author DuKaixiang
 * @date 2019/1/14.
 */
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public Set<String> findPower(String id) {
        return userMapper.selectById(id);
    }

    public User findUser(String username) {
        return userMapper.selectByUserName(username);
    }

    public int addCaptcha(Captcha captcha) {
        return userMapper.addCaptcha(captcha);
    }

    public int updateCaptcha(Captcha captcha) {
        return userMapper.updateCaptcha(captcha);
    }

    public Captcha getCaptcha(String uuid) {
        return userMapper.getCaptcha(uuid);
    }

    public int delCaptcha(String uuid) {
        return userMapper.delCaptcha(uuid);
    }

    public boolean register(String username, String password) {
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt=randomNumberGenerator.nextBytes().toHex();
        //指定散列算法为md5
        String algorithmName = "MD5";
        //散列迭代次数
        int hashIterations = 2;
        User user=new User();
        user.setSalt(salt);
        user.setUsername(username);
        user.setPassword(password);
        String newPassword =new SimpleHash(algorithmName,user.getPassword(), ByteSource.Util.bytes(user.getUsername()+salt),hashIterations).toHex();
        user.setPassword(newPassword);
        if(userMapper.selectByUserName(username)!=null){
            return false;
        }
        if(userMapper.insertSelective(user)==1){
            return true;
        }
        return false;
    }
}
