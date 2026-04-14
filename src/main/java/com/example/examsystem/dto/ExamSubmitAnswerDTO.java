package com.example.examsystem.dto;

import lombok.Data;

@Data
public class ExamSubmitAnswerDTO {

    private Integer questionId;

    private String userAnswer;
}