package com.example.examsystem.service;

import com.example.examsystem.vo.ClassCourseTeacherVO;

import java.util.List;

public interface ClassCourseTeacherService {

    void add(Integer classId, Integer courseId, Long teacherId);

    void delete(Integer id);

    List<ClassCourseTeacherVO> list();

    List<ClassCourseTeacherVO> getByClassId(Integer classId);

    List<ClassCourseTeacherVO> getByTeacherId(Long teacherId);

    List<ClassCourseTeacherVO> getByCourseId(Integer courseId);
}