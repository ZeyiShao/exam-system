package com.example.examsystem.vo;

import lombok.Data;

@Data
public class StudentClassVO {

    private Integer id;

    private Long studentId;
    private String studentUsername;
    private String studentRealName;

    private Integer classId;
    private String className;
}