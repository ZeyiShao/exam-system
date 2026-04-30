package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.examsystem.common.BusinessException;
import com.example.examsystem.entity.Course;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.entity.TeacherCourse;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.mapper.TeacherCourseMapper;
import com.example.examsystem.service.TeacherCourseService;
import com.example.examsystem.vo.TeacherCourseVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {

    private final TeacherCourseMapper teacherCourseMapper;
    private final SysUserMapper sysUserMapper;
    private final CourseMapper courseMapper;

    public TeacherCourseServiceImpl(TeacherCourseMapper teacherCourseMapper,
                                    SysUserMapper sysUserMapper,
                                    CourseMapper courseMapper) {
        this.teacherCourseMapper = teacherCourseMapper;
        this.sysUserMapper = sysUserMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public void add(Long teacherId, Integer courseId) {
        SysUser user = sysUserMapper.selectById(teacherId);
        if (user == null) {
            throw new BusinessException("教师不存在");
        }
        if (!"TEACHER".equals(user.getRole())) {
            throw new BusinessException("该用户不是教师");
        }

        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }

        LambdaQueryWrapper<TeacherCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherCourse::getTeacherId, teacherId)
                .eq(TeacherCourse::getCourseId, courseId);

        Long count = teacherCourseMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException("该教师已分配此课程");
        }

        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setTeacherId(teacherId);
        teacherCourse.setCourseId(courseId);

        teacherCourseMapper.insert(teacherCourse);
    }

    @Override
    public void delete(Integer id) {
        TeacherCourse teacherCourse = teacherCourseMapper.selectById(id);
        if (teacherCourse == null) {
            throw new BusinessException("关系不存在");
        }
        teacherCourseMapper.deleteById(id);
    }

    @Override
    public List<TeacherCourseVO> list() {
        return teacherCourseMapper.selectAllDetail();
    }

    @Override
    public List<TeacherCourseVO> getByTeacherId(Long teacherId) {
        SysUser user = sysUserMapper.selectById(teacherId);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!"TEACHER".equals(user.getRole())) {
            throw new BusinessException("该用户不是教师");
        }

        return teacherCourseMapper.selectByTeacherId(teacherId);
    }

    @Override
    public List<TeacherCourseVO> getByCourseId(Integer courseId) {
        return teacherCourseMapper.selectByCourseId(courseId);
    }
}