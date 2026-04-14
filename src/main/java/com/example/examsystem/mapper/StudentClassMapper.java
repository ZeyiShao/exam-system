package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.StudentClass;
import com.example.examsystem.vo.StudentClassVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentClassMapper extends BaseMapper<StudentClass> {

    @Select("""
            SELECT sc.id,
                   sc.student_id AS studentId,
                   u.username AS studentUsername,
                   u.real_name AS studentRealName,
                   sc.class_id AS classId,
                   c.class_name AS className
            FROM student_class sc
            LEFT JOIN sys_user u ON sc.student_id = u.id
            LEFT JOIN class_info c ON sc.class_id = c.id
            ORDER BY sc.id DESC
            """)
    List<StudentClassVO> selectAllDetail();

    @Select("""
            SELECT sc.id,
                   sc.student_id AS studentId,
                   u.username AS studentUsername,
                   u.real_name AS studentRealName,
                   sc.class_id AS classId,
                   c.class_name AS className
            FROM student_class sc
            LEFT JOIN sys_user u ON sc.student_id = u.id
            LEFT JOIN class_info c ON sc.class_id = c.id
            WHERE sc.student_id = #{studentId}
            ORDER BY sc.id DESC
            """)
    List<StudentClassVO> selectByStudentId(Long studentId);

    @Select("""
            SELECT sc.id,
                   sc.student_id AS studentId,
                   u.username AS studentUsername,
                   u.real_name AS studentRealName,
                   sc.class_id AS classId,
                   c.class_name AS className
            FROM student_class sc
            LEFT JOIN sys_user u ON sc.student_id = u.id
            LEFT JOIN class_info c ON sc.class_id = c.id
            WHERE sc.class_id = #{classId}
            ORDER BY sc.id DESC
            """)
    List<StudentClassVO> selectByClassId(Integer classId);
}