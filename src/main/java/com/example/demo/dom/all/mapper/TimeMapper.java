package com.example.demo.dom.all.mapper;

import com.example.demo.dom.all.entity.Time;

public interface TimeMapper {
    int insert(Time record);

    int insertSelective(Time record);
}