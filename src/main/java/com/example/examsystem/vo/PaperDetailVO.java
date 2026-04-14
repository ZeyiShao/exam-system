package com.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaperDetailVO {

    private Integer id;
    private String paperName;
    private Integer totalScore;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;
    private LocalDateTime createTime;

    private List<PaperQuestionDetailVO> questionList;
}