package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.entity.Exam;

import java.util.List;

public interface ExamService {

    void add(Exam exam);

    void delete(Integer id);

    void update(Exam exam);

    Exam getById(Integer id);

    List<Exam> list();

    IPage<Exam> page(Integer pageNum,
                     Integer pageSize,
                     String examName,
                     String status);
}