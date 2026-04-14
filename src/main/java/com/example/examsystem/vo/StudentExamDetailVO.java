package com.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentExamDetailVO {

    private Integer recordId;

    private Integer examId;

    private String examName;

    private Integer paperId;

    private Integer duration;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<StudentExamQuestionVO> questionList;
}