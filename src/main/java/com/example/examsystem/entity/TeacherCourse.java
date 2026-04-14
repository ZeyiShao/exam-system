package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher_course")
public class TeacherCourse {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long teacherId;

    private Integer courseId;
}