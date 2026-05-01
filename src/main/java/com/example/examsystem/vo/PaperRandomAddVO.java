package com.example.examsystem.vo;

import lombok.Data;

@Data
public class PaperRandomAddVO {

    private String paperName;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;

    /**
     * 课程ID（用于筛选题目）
     */
    private Integer courseId;

    /**
     * 难度筛选（1简单 2普通 3困难）
     */
    private Integer difficulty;

    private Integer singleCount;
    private Integer multipleCount;
    private Integer judgeCount;

    private Integer singleScore;
    private Integer multipleScore;
    private Integer judgeScore;
}