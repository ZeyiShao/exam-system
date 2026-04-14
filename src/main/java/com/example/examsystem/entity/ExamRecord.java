package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam_record")
public class ExamRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer examId;

    private Long studentId;

    private Integer score;

    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    private String status;

    private Boolean isPass;
}