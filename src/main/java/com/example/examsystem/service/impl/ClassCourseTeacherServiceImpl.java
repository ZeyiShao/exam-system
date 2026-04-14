package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.examsystem.entity.ClassCourseTeacher;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.entity.Course;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.entity.TeacherCourse;
import com.example.examsystem.mapper.ClassCourseTeacherMapper;
import com.example.examsystem.mapper.ClassInfoMapper;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.mapper.TeacherCourseMapper;
import com.example.examsystem.service.ClassCourseTeacherService;
import com.example.examsystem.vo.ClassCourseTeacherVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassCourseTeacherServiceImpl implements ClassCourseTeacherService {

    private final ClassCourseTeacherMapper classCourseTeacherMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final SysUserMapper sysUserMapper;
    private final TeacherCourseMapper teacherCourseMapper;

    public ClassCourseTeacherServiceImpl(ClassCourseTeacherMapper classCourseTeacherMapper,
                                         ClassInfoMapper classInfoMapper,
                                         CourseMapper courseMapper,
                                         SysUserMapper sysUserMapper,
                                         TeacherCourseMapper teacherCourseMapper) {
        this.classCourseTeacherMapper = classCourseTeacherMapper;
        this.classInfoMapper = classInfoMapper;
        this.courseMapper = courseMapper;
        this.sysUserMapper = sysUserMapper;
        this.teacherCourseMapper = teacherCourseMapper;
    }

    @Override
    @Transactional
    public void add(Integer classId, Integer courseId, Long teacherId) {
        ClassInfo classInfo = classInfoMapper.selectById(classId);
        if (classInfo == null) {
            throw new RuntimeException("班级不存在");
        }

        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        SysUser teacher = sysUserMapper.selectById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        if (!"TEACHER".equals(teacher.getRole())) {
            throw new RuntimeException("该用户不是教师");
        }

        LambdaQueryWrapper<ClassCourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassCourseTeacher::getClassId, classId)
                .eq(ClassCourseTeacher::getCourseId, courseId)
                .eq(ClassCourseTeacher::getTeacherId, teacherId);

        Long count = classCourseTeacherMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new RuntimeException("该班级课程教师关系已存在");
        }

        ClassCourseTeacher relation = new ClassCourseTeacher();
        relation.setClassId(classId);
        relation.setCourseId(courseId);
        relation.setTeacherId(teacherId);
        classCourseTeacherMapper.insert(relation);

        LambdaQueryWrapper<TeacherCourse> teacherCourseWrapper = new LambdaQueryWrapper<>();
        teacherCourseWrapper.eq(TeacherCourse::getTeacherId, teacherId)
                .eq(TeacherCourse::getCourseId, courseId);

        Long teacherCourseCount = teacherCourseMapper.selectCount(teacherCourseWrapper);
        if (teacherCourseCount == null || teacherCourseCount == 0) {
            TeacherCourse teacherCourse = new TeacherCourse();
            teacherCourse.setTeacherId(teacherId);
            teacherCourse.setCourseId(courseId);
            teacherCourseMapper.insert(teacherCourse);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ClassCourseTeacher relation = classCourseTeacherMapper.selectById(id);
        if (relation == null) {
            throw new RuntimeException("关系不存在");
        }

        classCourseTeacherMapper.deleteById(id);

        LambdaQueryWrapper<ClassCourseTeacher> relationWrapper = new LambdaQueryWrapper<>();
        relationWrapper.eq(ClassCourseTeacher::getTeacherId, relation.getTeacherId())
                .eq(ClassCourseTeacher::getCourseId, relation.getCourseId());

        Long remainCount = classCourseTeacherMapper.selectCount(relationWrapper);

        if (remainCount == null || remainCount == 0) {
            LambdaQueryWrapper<TeacherCourse> teacherCourseWrapper = new LambdaQueryWrapper<>();
            teacherCourseWrapper.eq(TeacherCourse::getTeacherId, relation.getTeacherId())
                    .eq(TeacherCourse::getCourseId, relation.getCourseId());
            teacherCourseMapper.delete(teacherCourseWrapper);
        }
    }

    @Override
    public List<ClassCourseTeacherVO> list() {
        return classCourseTeacherMapper.selectAllDetail();
    }

    @Override
    public List<ClassCourseTeacherVO> getByClassId(Integer classId) {
        ClassInfo classInfo = classInfoMapper.selectById(classId);
        if (classInfo == null) {
            throw new RuntimeException("班级不存在");
        }
        return classCourseTeacherMapper.selectByClassId(classId);
    }

    @Override
    public List<ClassCourseTeacherVO> getByTeacherId(Long teacherId) {
        SysUser teacher = sysUserMapper.selectById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        if (!"TEACHER".equals(teacher.getRole())) {
            throw new RuntimeException("该用户不是教师");
        }
        return classCourseTeacherMapper.selectByTeacherId(teacherId);
    }

    @Override
    public List<ClassCourseTeacherVO> getByCourseId(Integer courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        return classCourseTeacherMapper.selectByCourseId(courseId);
    }
}