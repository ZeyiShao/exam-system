package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.Exam;

import java.util.List;

public interface ExamService {

    void add(Exam exam);

    void delete(Integer id);

    void update(Exam exam);

    void approve(Integer id, Long auditUser);

    void reject(Integer id, Long auditUser, String rejectReason);

    void submitExam(ExamSubmitDTO dto);

    Exam getById(Integer id);

    List<Exam> list();

    IPage<Exam> page(Integer pageNum,
                     Integer pageSize,
                     String examName,
                     String status);

    IPage<Exam> reviewPage(Integer pageNum,
                           Integer pageSize,
                           String examName);

    IPage<Exam> myPage(Integer pageNum,
                       Integer pageSize,
                       Long teacherId,
                       String auditStatus,
                       String examName);
}