package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("exam_answer")
public class ExamAnswer {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer recordId;

    private Integer questionId;

    private String userAnswer;

    private String correctAnswer;

    private Boolean isCorrect;

    private Integer score;
}