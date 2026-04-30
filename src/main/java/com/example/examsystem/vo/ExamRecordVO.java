package com.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamRecordVO {

    private Integer recordId;

    private Integer examId;

    private String examName;

    private Integer score;

    private Boolean isPass;

    private LocalDateTime submitTime;
}