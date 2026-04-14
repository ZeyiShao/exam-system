package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.entity.StudentClass;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.mapper.ClassInfoMapper;
import com.example.examsystem.mapper.StudentClassMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.service.StudentClassService;
import com.example.examsystem.vo.StudentClassVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentClassServiceImpl implements StudentClassService {

    private final StudentClassMapper studentClassMapper;
    private final SysUserMapper sysUserMapper;
    private final ClassInfoMapper classInfoMapper;

    public StudentClassServiceImpl(StudentClassMapper studentClassMapper,
                                   SysUserMapper sysUserMapper,
                                   ClassInfoMapper classInfoMapper) {
        this.studentClassMapper = studentClassMapper;
        this.sysUserMapper = sysUserMapper;
        this.classInfoMapper = classInfoMapper;
    }

    @Override
    public void add(Long studentId, Integer classId) {
        SysUser user = sysUserMapper.selectById(studentId);
        if (user == null) {
            throw new RuntimeException("学生不存在");
        }
        if (!"STUDENT".equals(user.getRole())) {
            throw new RuntimeException("该用户不是学生");
        }

        ClassInfo classInfo = classInfoMapper.selectById(classId);
        if (classInfo == null) {
            throw new RuntimeException("班级不存在");
        }

        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentId)
                .eq(StudentClass::getClassId, classId);

        Long count = studentClassMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new RuntimeException("该学生已加入此班级");
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(studentId);
        studentClass.setClassId(classId);

        studentClassMapper.insert(studentClass);
    }

    @Override
    public void delete(Integer id) {
        StudentClass studentClass = studentClassMapper.selectById(id);
        if (studentClass == null) {
            throw new RuntimeException("关系不存在");
        }
        studentClassMapper.deleteById(id);
    }

    @Override
    public List<StudentClassVO> list() {
        return studentClassMapper.selectAllDetail();
    }

    @Override
    public List<StudentClassVO> getByStudentId(Long studentId) {
        return studentClassMapper.selectByStudentId(studentId);
    }

    @Override
    public List<StudentClassVO> getByClassId(Integer classId) {
        return studentClassMapper.selectByClassId(classId);
    }
}