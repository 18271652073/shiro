package com.example.demo.controller;

import com.example.demo.config.RedisClient;
import com.example.demo.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @date 2019/2/28.
 */
@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/lock", method = RequestMethod.GET)
    public ResultEntity lock(String username) {
        redisClient.remove(username);
        return ResultEntity.ok("success");
    }
}
