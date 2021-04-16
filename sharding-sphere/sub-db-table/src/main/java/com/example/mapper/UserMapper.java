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
     */
    List<User> selectAll();

    /**
     * 插入一条记录
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertForeach(List<User> list);

}