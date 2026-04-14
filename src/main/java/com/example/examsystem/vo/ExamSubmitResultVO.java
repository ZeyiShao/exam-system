package com.example.examsystem.vo;

import lombok.Data;

@Data
public class ExamSubmitResultVO {

    private Integer recordId;

    private Integer score;

    private Boolean isPass;

    private String status;
}