package com.example.demo.dom.all.mapper;

import com.example.demo.dom.all.entity.Captcha;
import com.example.demo.dom.all.entity.User;

import java.util.Set;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Set<String> selectById(String id);

    User selectByUserName(String username);

    int addCaptcha(Captcha captcha);

    int updateCaptcha(Captcha captcha);

    Captcha getCaptcha(String uuid);

    int delCaptcha(String uuid);
}