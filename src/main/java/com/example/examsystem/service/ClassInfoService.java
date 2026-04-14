package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.entity.ClassInfo;

import java.util.List;

public interface ClassInfoService {

    void add(ClassInfo classInfo);

    void delete(Integer id);

    void update(ClassInfo classInfo);

    ClassInfo getById(Integer id);

    List<ClassInfo> list();

    IPage<ClassInfo> page(Integer pageNum, Integer pageSize, String className);
}