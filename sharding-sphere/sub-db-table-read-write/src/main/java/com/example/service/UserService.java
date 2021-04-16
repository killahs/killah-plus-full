package com.example.service;


import com.example.entity.User;

import java.util.List;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 用户接口
 */
public interface UserService {

    /**
     * 获取所有用户信息
     */
    List<User>  list();

    /**
     * 批量保存用户信息
     * @param users
     * @return
     */
    String  insertForeach(List<User> users);

}