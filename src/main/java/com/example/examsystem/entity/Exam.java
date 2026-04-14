package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam")
public class Exam {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String examName;

    private Integer paperId;

    private Integer classId;

    private Integer courseId;

    private Long teacherId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration;

    private String status;

    private LocalDateTime createTime;
}