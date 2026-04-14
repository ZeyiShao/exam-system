package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_class")
public class StudentClass {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long studentId;

    private Integer classId;
}