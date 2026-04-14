package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("class_course_teacher")
public class ClassCourseTeacher {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer classId;

    private Integer courseId;

    private Long teacherId;
}