package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.TeacherCourse;
import com.example.examsystem.vo.TeacherCourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherCourseMapper extends BaseMapper<TeacherCourse> {

    @Select("""
            SELECT tc.id,
                   tc.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName,
                   tc.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode
            FROM teacher_course tc
            LEFT JOIN sys_user u ON tc.teacher_id = u.id
            LEFT JOIN course c ON tc.course_id = c.id
            ORDER BY tc.id DESC
            """)
    List<TeacherCourseVO> selectAllDetail();

    @Select("""
            SELECT tc.id,
                   tc.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName,
                   tc.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode
            FROM teacher_course tc
            LEFT JOIN sys_user u ON tc.teacher_id = u.id
            LEFT JOIN course c ON tc.course_id = c.id
            WHERE tc.teacher_id = #{teacherId}
            ORDER BY tc.id DESC
            """)
    List<TeacherCourseVO> selectByTeacherId(Long teacherId);

    @Select("""
            SELECT tc.id,
                   tc.teacher_id AS teacherId,
                   u.username AS teacherUsername,
                   u.real_name AS teacherRealName,
                   tc.course_id AS courseId,
                   c.course_name AS courseName,
                   c.course_code AS courseCode
            FROM teacher_course tc
            LEFT JOIN sys_user u ON tc.teacher_id = u.id
            LEFT JOIN course c ON tc.course_id = c.id
            WHERE tc.course_id = #{courseId}
            ORDER BY tc.id DESC
            """)
    List<TeacherCourseVO> selectByCourseId(Integer courseId);
}