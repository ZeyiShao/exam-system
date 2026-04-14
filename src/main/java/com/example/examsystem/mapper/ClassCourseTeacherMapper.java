package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.ClassCourseTeacher;
import com.example.examsystem.vo.ClassCourseTeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassCourseTeacherMapper extends BaseMapper<ClassCourseTeacher> {

    @Select("""
            SELECT cct.id,
                   cct.class_id AS classId,
                   ci.class_name AS className,
                   cct.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode,
                   cct.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName
            FROM class_course_teacher cct
            LEFT JOIN class_info ci ON cct.class_id = ci.id
            LEFT JOIN course c ON cct.course_id = c.id
            LEFT JOIN sys_user u ON cct.teacher_id = u.id
            ORDER BY cct.id DESC
            """)
    List<ClassCourseTeacherVO> selectAllDetail();

    @Select("""
            SELECT cct.id,
                   cct.class_id AS classId,
                   ci.class_name AS className,
                   cct.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode,
                   cct.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName
            FROM class_course_teacher cct
            LEFT JOIN class_info ci ON cct.class_id = ci.id
            LEFT JOIN course c ON cct.course_id = c.id
            LEFT JOIN sys_user u ON cct.teacher_id = u.id
            WHERE cct.class_id = #{classId}
            ORDER BY cct.id DESC
            """)
    List<ClassCourseTeacherVO> selectByClassId(@Param("classId") Integer classId);

    @Select("""
            SELECT cct.id,
                   cct.class_id AS classId,
                   ci.class_name AS className,
                   cct.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode,
                   cct.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName
            FROM class_course_teacher cct
            LEFT JOIN class_info ci ON cct.class_id = ci.id
            LEFT JOIN course c ON cct.course_id = c.id
            LEFT JOIN sys_user u ON cct.teacher_id = u.id
            WHERE cct.teacher_id = #{teacherId}
            ORDER BY cct.id DESC
            """)
    List<ClassCourseTeacherVO> selectByTeacherId(@Param("teacherId") Long teacherId);

    @Select("""
            SELECT cct.id,
                   cct.class_id AS classId,
                   ci.class_name AS className,
                   cct.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode,
                   cct.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName
            FROM class_course_teacher cct
            LEFT JOIN class_info ci ON cct.class_id = ci.id
            LEFT JOIN course c ON cct.course_id = c.id
            LEFT JOIN sys_user u ON cct.teacher_id = u.id
            WHERE cct.course_id = #{courseId}
            ORDER BY cct.id DESC
            """)
    List<ClassCourseTeacherVO> selectByCourseId(@Param("courseId") Integer courseId);
}