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

    private String questionType; // single / multiple / judge

    private Integer difficulty;

    private String course;

    private Integer createUser;

    private LocalDateTime createTime;
}