package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.entity.Course;

import java.util.List;

public interface CourseService {

    void add(Course course);

    void delete(Integer id);

    void update(Course course);

    Course getById(Integer id);

    List<Course> list();

    IPage<Course> page(Integer pageNum, Integer pageSize, String courseName);
}