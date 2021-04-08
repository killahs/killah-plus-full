package com.example.controller;


import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Killah
 * @Version: 1.0
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @Description: 获取用户列表
     */
    @GetMapping("user/list")
    public Object userList() {
        return userService.list();
    }

    /**
     * @Description: 保存用户
     */
    @PostMapping("user/save")
    public Object saveUser() {
        return userService.saveOne(new User("小小", "女", 3));
    }



}
