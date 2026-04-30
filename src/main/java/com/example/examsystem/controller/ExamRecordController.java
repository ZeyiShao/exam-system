package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.dto.ExamStartDTO;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.ExamRecord;
import com.example.examsystem.service.ExamRecordService;
import com.example.examsystem.vo.ExamSubmitResultVO;
import com.example.examsystem.vo.StudentExamDetailVO;
import org.springframework.web.bind.annotation.*;
import com.example.examsystem.vo.ExamRecordVO;
import com.example.examsystem.vo.ExamRecordTeacherVO;
import com.example.examsystem.vo.ExamStatisticsVO;

import java.util.List;

@RestController
@RequestMapping("/examRecord")
public class ExamRecordController {

    private final ExamRecordService examRecordService;

    public ExamRecordController(ExamRecordService examRecordService) {
        this.examRecordService = examRecordService;
    }

    @PostMapping("/start")
    public Result<Integer> startExam(@RequestBody ExamStartDTO dto) {
        Integer recordId = examRecordService.startExam(dto);
        return Result.success("开始考试成功", recordId);
    }

    @GetMapping("/detail/{recordId}")
    public Result<StudentExamDetailVO> getExamDetail(@PathVariable Integer recordId) {
        return Result.success("查询成功", examRecordService.getExamDetail(recordId));
    }

    @PostMapping("/submit")
    public Result<ExamSubmitResultVO> submitExam(@RequestBody ExamSubmitDTO dto) {
        return Result.success("交卷成功", examRecordService.submitExam(dto));
    }

    @GetMapping("/{id}")
    public Result<ExamRecord> getById(@PathVariable Integer id) {
        return Result.success("查询成功", examRecordService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public Result<List<ExamRecordVO>> getByStudentId(@PathVariable Long studentId) {
        return Result.success("查询成功", examRecordService.getByStudentId(studentId));
    }

    @GetMapping("/exam/{examId}")
    public Result<List<ExamRecordTeacherVO>> getByExamId(@PathVariable Integer examId) {
        return Result.success("查询成功", examRecordService.getByExamId(examId));
    }

    @GetMapping("/statistics/{examId}")
    public Result<ExamStatisticsVO> getStatisticsByExamId(@PathVariable Integer examId) {
        return Result.success("查询成功", examRecordService.getStatisticsByExamId(examId));
    }
}