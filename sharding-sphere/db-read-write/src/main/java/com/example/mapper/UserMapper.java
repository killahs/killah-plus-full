package com.example.mapper;


import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Killah
 * @Version: 1.0
 */
@Mapper
public interface UserMapper {

    /**
     * 获取所有用户
     * @return
     */
    List<User> selectAll();

    /**
     * 插入一条记录
     * @param record
     * @return
     */
    default int insert(User record) {
        return 0;
    }
}