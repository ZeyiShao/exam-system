package com.example.examsystem.service;

import com.example.examsystem.dto.ExamStartDTO;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.ExamRecord;
import com.example.examsystem.vo.ExamSubmitResultVO;
import com.example.examsystem.vo.StudentExamDetailVO;

import java.util.List;

public interface ExamRecordService {

    Integer startExam(ExamStartDTO dto);

    StudentExamDetailVO getExamDetail(Integer recordId);

    ExamSubmitResultVO submitExam(ExamSubmitDTO dto);

    ExamRecord getById(Integer id);

    List<ExamRecord> getByStudentId(Long studentId);

    List<ExamRecord> getByExamId(Integer examId);
}