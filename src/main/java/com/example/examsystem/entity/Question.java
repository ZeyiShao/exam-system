package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Question {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;

    /**
     * 题型：single / multiple / judge
     */
    private String questionType;

    /**
     * 难度：1简单 / 2普通 / 3困难
     */
    private Integer difficulty;

    /**
     * 课程ID，正式用于课程关联和筛选
     */
    private Integer courseId;

    /**
     * 课程名称，主要用于页面展示和兼容旧数据
     */
    private String course;

    private Integer createUser;

    private LocalDateTime createTime;
}