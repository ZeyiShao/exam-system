package com.example.examsystem.service;

import com.example.examsystem.vo.StudentClassVO;

import java.util.List;

public interface StudentClassService {

    void add(Long studentId, Integer classId);

    void delete(Integer id);

    List<StudentClassVO> list();

    List<StudentClassVO> getByStudentId(Long studentId);

    List<StudentClassVO> getByClassId(Integer classId);
}