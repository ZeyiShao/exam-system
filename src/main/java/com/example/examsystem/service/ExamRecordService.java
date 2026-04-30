package com.example.examsystem.service;

import com.example.examsystem.dto.ExamStartDTO;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.ExamRecord;
import com.example.examsystem.vo.ExamRecordVO;
import com.example.examsystem.vo.ExamSubmitResultVO;
import com.example.examsystem.vo.StudentExamDetailVO;
import com.example.examsystem.vo.ExamRecordTeacherVO;
import com.example.examsystem.vo.ExamStatisticsVO;

import java.util.List;

public interface ExamRecordService {

    Integer startExam(ExamStartDTO dto);

    StudentExamDetailVO getExamDetail(Integer recordId);

    ExamSubmitResultVO submitExam(ExamSubmitDTO dto);

    ExamRecord getById(Integer id);

    List<ExamRecordVO> getByStudentId(Long studentId);

    List<ExamRecordTeacherVO> getByExamId(Integer examId);

    ExamStatisticsVO getStatisticsByExamId(Integer examId);
}