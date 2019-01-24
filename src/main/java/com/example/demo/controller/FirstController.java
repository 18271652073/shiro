package com.example.demo.controller;

import com.example.demo.dom.all.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author DuKaixiang
 * @date 2019/1/15.
 */
@Controller
@RequestMapping(value = "/home")
public class FirstController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    public String first(Model model) {
        Subject subject = SecurityUtils.getSubject();
        User admin=(User) subject.getPrincipal();
        User user=userService.findUser(admin.getUsername());
        model.addAttribute("username",user.getUsername());
        return "first";
    }
}
