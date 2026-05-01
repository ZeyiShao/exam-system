package com.example.examsystem.vo;

import lombok.Data;

@Data
public class PaperRandomAddVO {

    private String paperName;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;

    /**
     * 课程筛选条件，可为空。
     * 为空时表示不按课程限制随机抽题。
     */
    private String course;

    /**
     * 难度筛选条件，可为空。
     * 为空时表示不按难度限制随机抽题。
     */
    private Integer difficulty;

    private Integer singleCount;
    private Integer multipleCount;
    private Integer judgeCount;

    private Integer singleScore;
    private Integer multipleScore;
    private Integer judgeScore;
}