package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PaperQuestion {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer paperId;

    private Integer questionId;

    private Integer score;
}