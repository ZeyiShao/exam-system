package com.example.examsystem.service;

import com.example.examsystem.vo.TeacherCourseVO;

import java.util.List;

public interface TeacherCourseService {

    void add(Long teacherId, Integer courseId);

    void delete(Integer id);

    List<TeacherCourseVO> list();

    List<TeacherCourseVO> getByTeacherId(Long teacherId);

    List<TeacherCourseVO> getByCourseId(Integer courseId);
}