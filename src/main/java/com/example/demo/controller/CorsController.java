package com.example.demo.controller;

import com.example.demo.config.UserToken;
import com.example.demo.dom.all.entity.User;
import com.example.demo.service.CaptchaService;
import com.example.demo.service.UserService;
import com.example.demo.util.ResultEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author DuKaixiang
 * @date 2019/1/22.
 */

@RestController
//@CrossOrigin
@RequestMapping(value = "/testLogin")
public class CorsController {
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    public ResultEntity loginPage(Model model) {
        model.addAttribute("name", "FreeMarker 模版引擎 ");
        model.addAttribute("msg", "请登录或注册！");
        return ResultEntity.ok("进入页面");
    }

    /**
     * 登录验证
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResultEntity check(HttpServletRequest request, Model model,
                        @RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false,defaultValue = "") String captcha,
                        @RequestParam(required = false,defaultValue = "") String rememberMe) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        Subject subject = SecurityUtils.getSubject();
        String sessionId=request.getSession().getId();
        System.out.println(sessionId);
//        if(!(captchaService.validate(sessionId,captcha))){
//            return ResultEntity.error("验证码输入错误或超时！请刷新页面！");
//        }
        UserToken userToken = new UserToken(username, password,"Admin");
        if(rememberMe!=null&&"".equals(rememberMe)){
            userToken.setRememberMe(true);
        }
        subject.login(userToken);
        return ResultEntity.ok("登陆成功！");
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ResultEntity register(Model model, @RequestParam String username, @RequestParam String password) {
        if(userService.register(username,password)){
            return ResultEntity.ok("注册成功");
        }else {
            return ResultEntity.error("注册失败，用户已存在！");
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public ResultEntity loginOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultEntity.ok("登出成功！");
    }

    /**
     * 验证码刷新
     */
    @GetMapping(value = "/captcha.img")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId=request.getSession().getId();
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        BufferedImage image=captchaService.getKaptcha(sessionId);
        ServletOutputStream out=response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @RequestMapping(value = "/first", method = RequestMethod.GET)
    public ResultEntity first(Model model) {
        Subject subject = SecurityUtils.getSubject();
        User admin=(User) subject.getPrincipal();
        User user=userService.findUser(admin.getUsername());
        model.addAttribute("username",user.getUsername());
        return ResultEntity.ok(user.toString());
    }
}
