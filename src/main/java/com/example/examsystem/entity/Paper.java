package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Paper {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String paperName;

    private Integer totalScore;

    private Integer duration;

    private Integer passScore;

    private Integer createUser;

    private LocalDateTime createTime;
}