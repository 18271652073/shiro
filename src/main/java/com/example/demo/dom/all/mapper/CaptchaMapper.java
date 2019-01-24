package com.example.demo.dom.all.mapper;

import com.example.demo.dom.all.entity.Captcha;

public interface CaptchaMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(Captcha record);

    int insertSelective(Captcha record);

    Captcha selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(Captcha record);

    int updateByPrimaryKey(Captcha record);
}