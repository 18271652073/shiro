package com.example.demo.controller;

import com.example.demo.config.UserToken;
import com.example.demo.service.CaptchaService;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @date 2019/1/11.
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    public String loginPage(Model model) {
        model.addAttribute("name", "FreeMarker 模版引擎 ");
        model.addAttribute("msg", "请登录或注册！");
        return "login";
    }

    /**
     * 登录验证
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check(HttpServletRequest request, Model model,
                        @RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String captcha,
                        @RequestParam(required = false, defaultValue = "") String rememberMe) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        Subject subject = SecurityUtils.getSubject();
        String sessionId = request.getSession().getId();
        if (!(captchaService.validate(sessionId, captcha))) {
            model.addAttribute("msg", "验证码输入错误或超时！请刷新页面！");
            return "login";
        }
        UserToken userToken = new UserToken(username, password, "Admin", "yes");
        if (rememberMe != null && "".equals(rememberMe)) {
            userToken.setRememberMe(true);
        }
        subject.login(userToken);
        return "home";
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model, @RequestParam String username, @RequestParam String password) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        if (userService.register(username, password)) {
            model.addAttribute("msg", "注册成功");
        } else {
            model.addAttribute("msg", "注册失败，用户已存在！");
        }
        return "login";
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public String loginOut() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    /**
     * 验证码刷新
     */
    @ResponseBody
    @GetMapping(value = "/captcha.img")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getSession().getId();
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        BufferedImage image = captchaService.getKaptcha(sessionId);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }
}
