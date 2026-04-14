package com.example.examsystem.vo;

import lombok.Data;

import java.util.List;

@Data
public class PaperAddVO {
    private Integer id;

    private String paperName;
    private Integer totalScore;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;

    private List<PaperQuestionVO> questionList;
}