package com.example.demo.dom.all.mapper;

import com.example.demo.dom.all.entity.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}