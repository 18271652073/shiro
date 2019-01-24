package com.example.demo.service;

import com.example.demo.dom.all.entity.Captcha;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * @author Administrator
 * @date 2018/9/12.
 */
@Service
public class CaptchaService {
    @Autowired
    private Producer producer;
    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(CaptchaService.class);

    public BufferedImage getKaptcha(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            logger.error("uuid不能为空！");
        }
        String code = producer.createText();
        Date date = new Date((new Date()).getTime() + 300000);
        Captcha captcha = new Captcha();
        captcha.setCode(code);
        captcha.setTime(date);
        captcha.setUuid(uuid);
        if(userService.getCaptcha(uuid)!=null){
            userService.updateCaptcha(captcha);
        }else {
            userService.addCaptcha(captcha);
        }
        return producer.createImage(code);
    }

    public boolean validate(String uuid, String code) {
        boolean result=false;
        Captcha captcha = userService.getCaptcha(uuid);
        if(captcha==null){
            result=false;
        }else if(code.equals(captcha.getCode()) &&  captcha.getTime().getTime()>(new Date()).getTime()){
            userService.delCaptcha(uuid);
            result=true;
        }
        return result;
    }
}
