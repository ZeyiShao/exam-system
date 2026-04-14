package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Course {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String courseName;

    private String courseCode;

    private String description;

    private String status;

    private LocalDateTime createTime;
}