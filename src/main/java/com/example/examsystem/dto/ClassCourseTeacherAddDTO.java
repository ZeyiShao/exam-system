package com.example.examsystem.dto;

import lombok.Data;

@Data
public class ClassCourseTeacherAddDTO {

    private Integer classId;

    private Integer courseId;

    private Long teacherId;
}