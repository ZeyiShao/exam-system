package com.example.examsystem.vo;

import lombok.Data;

@Data
public class TeacherCourseVO {

    private Integer id;

    private Long teacherId;
    private String teacherUsername;
    private String teacherRealName;

    private Integer courseId;
    private String courseName;
    private String courseCode;
}