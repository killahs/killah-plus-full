package com.example.service.impl;


import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 用户实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public  List<User> list() {
        List<User> users = userMapper.selectAll();
        return users;
    }

    @Override
    public String insertForeach(List<User> users) {
        userMapper.insertForeach(users);
        return "保存成功";
    }

}
