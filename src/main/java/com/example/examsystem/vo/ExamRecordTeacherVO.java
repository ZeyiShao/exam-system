package com.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamRecordTeacherVO {

    private Integer recordId;

    private Integer examId;

    private Long studentId;

    private String studentName;

    private Integer score;

    private Boolean isPass;

    private LocalDateTime submitTime;
}