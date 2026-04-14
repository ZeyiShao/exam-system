package com.example.examsystem.vo;

import lombok.Data;

@Data
public class PaperRandomAddVO {

    private String paperName;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;

    private Integer singleCount;
    private Integer multipleCount;
    private Integer judgeCount;

    private Integer singleScore;
    private Integer multipleScore;
    private Integer judgeScore;
}