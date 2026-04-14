package com.example.examsystem.vo;

import lombok.Data;

@Data
public class StudentExamQuestionVO {

    private Integer questionId;

    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String questionType;

    private Integer score;
}