package com.example.examsystem.vo;

import lombok.Data;

@Data
public class PaperQuestionDetailVO {

    private Integer questionId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String questionType;
    private Integer difficulty;
    private String course;
    private Integer score;
}