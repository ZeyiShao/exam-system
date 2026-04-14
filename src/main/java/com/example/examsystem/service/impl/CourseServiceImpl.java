package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.entity.Course;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public void add(Course course) {
        courseMapper.insert(course);
    }

    @Override
    public void delete(Integer id) {
        courseMapper.deleteById(id);
    }

    @Override
    public void update(Course course) {
        courseMapper.updateById(course);
    }

    @Override
    public Course getById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<Course> list() {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Course::getId);
        return courseMapper.selectList(wrapper);
    }

    @Override
    public IPage<Course> page(Integer pageNum, Integer pageSize, String courseName) {
        Page<Course> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(courseName)) {
            wrapper.like(Course::getCourseName, courseName);
        }
        wrapper.orderByDesc(Course::getId);

        return courseMapper.selectPage(page, wrapper);
    }
}