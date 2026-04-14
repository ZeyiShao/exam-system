package com.example.examsystem.vo;

import lombok.Data;

@Data
public class ClassCourseTeacherVO {

    private Integer id;

    private Integer classId;
    private String className;

    private Integer courseId;
    private String courseName;
    private String courseCode;

    private Long teacherId;
    private String teacherUsername;
    private String teacherRealName;
}