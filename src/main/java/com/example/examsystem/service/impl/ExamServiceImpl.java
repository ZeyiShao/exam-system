package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.entity.ClassCourseTeacher;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.entity.Course;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.Paper;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.mapper.ClassCourseTeacherMapper;
import com.example.examsystem.mapper.ClassInfoMapper;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.mapper.PaperMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;
    private final PaperMapper paperMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final SysUserMapper sysUserMapper;
    private final ClassCourseTeacherMapper classCourseTeacherMapper;

    public ExamServiceImpl(ExamMapper examMapper,
                           PaperMapper paperMapper,
                           ClassInfoMapper classInfoMapper,
                           CourseMapper courseMapper,
                           SysUserMapper sysUserMapper,
                           ClassCourseTeacherMapper classCourseTeacherMapper) {
        this.examMapper = examMapper;
        this.paperMapper = paperMapper;
        this.classInfoMapper = classInfoMapper;
        this.courseMapper = courseMapper;
        this.sysUserMapper = sysUserMapper;
        this.classCourseTeacherMapper = classCourseTeacherMapper;
    }

    @Override
    public void add(Exam exam) {
        validateExam(exam, false);

        if (!StringUtils.hasText(exam.getStatus())) {
            exam.setStatus("NOT_STARTED");
        }

        examMapper.insert(exam);
    }

    @Override
    public void delete(Integer id) {
        Exam exam = examMapper.selectById(id);
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }
        examMapper.deleteById(id);
    }

    @Override
    public void update(Exam exam) {
        if (exam.getId() == null) {
            throw new RuntimeException("考试ID不能为空");
        }

        Exam oldExam = examMapper.selectById(exam.getId());
        if (oldExam == null) {
            throw new RuntimeException("考试不存在");
        }

        validateExam(exam, true);

        if (!StringUtils.hasText(exam.getStatus())) {
            exam.setStatus(oldExam.getStatus());
        }

        examMapper.updateById(exam);
    }

    @Override
    public Exam getById(Integer id) {
        Exam exam = examMapper.selectById(id);
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }
        return exam;
    }

    @Override
    public List<Exam> list() {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Exam::getId);
        return examMapper.selectList(wrapper);
    }

    @Override
    public IPage<Exam> page(Integer pageNum,
                            Integer pageSize,
                            String examName,
                            String status) {
        Page<Exam> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(examName)) {
            wrapper.like(Exam::getExamName, examName);
        }

        if (StringUtils.hasText(status)) {
            wrapper.eq(Exam::getStatus, status);
        }

        wrapper.orderByDesc(Exam::getId);

        return examMapper.selectPage(page, wrapper);
    }

    private void validateExam(Exam exam, boolean isUpdate) {
        if (!StringUtils.hasText(exam.getExamName())) {
            throw new RuntimeException("考试名称不能为空");
        }

        if (exam.getPaperId() == null) {
            throw new RuntimeException("试卷ID不能为空");
        }

        if (exam.getClassId() == null) {
            throw new RuntimeException("班级ID不能为空");
        }

        if (exam.getCourseId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }

        if (exam.getTeacherId() == null) {
            throw new RuntimeException("教师ID不能为空");
        }

        if (exam.getStartTime() == null || exam.getEndTime() == null) {
            throw new RuntimeException("开始时间和结束时间不能为空");
        }

        if (!exam.getStartTime().isBefore(exam.getEndTime())) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }

        if (exam.getDuration() == null || exam.getDuration() <= 0) {
            throw new RuntimeException("考试时长必须大于0");
        }

        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw new RuntimeException("试卷不存在");
        }

        ClassInfo classInfo = classInfoMapper.selectById(exam.getClassId());
        if (classInfo == null) {
            throw new RuntimeException("班级不存在");
        }

        Course course = courseMapper.selectById(exam.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        SysUser teacher = sysUserMapper.selectById(exam.getTeacherId());
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }

        if (!"TEACHER".equals(teacher.getRole())) {
            throw new RuntimeException("该用户不是教师");
        }

        LambdaQueryWrapper<ClassCourseTeacher> relationWrapper = new LambdaQueryWrapper<>();
        relationWrapper.eq(ClassCourseTeacher::getClassId, exam.getClassId())
                .eq(ClassCourseTeacher::getCourseId, exam.getCourseId())
                .eq(ClassCourseTeacher::getTeacherId, exam.getTeacherId());

        Long relationCount = classCourseTeacherMapper.selectCount(relationWrapper);
        if (relationCount == null || relationCount == 0) {
            throw new RuntimeException("该教师未负责此班级的该课程，不能创建考试");
        }

        LambdaQueryWrapper<Exam> examWrapper = new LambdaQueryWrapper<>();
        examWrapper.eq(Exam::getClassId, exam.getClassId())
                .eq(Exam::getCourseId, exam.getCourseId())
                .eq(Exam::getTeacherId, exam.getTeacherId())
                .eq(Exam::getStartTime, exam.getStartTime())
                .eq(Exam::getEndTime, exam.getEndTime());

        if (isUpdate) {
            examWrapper.ne(Exam::getId, exam.getId());
        }

        Long count = examMapper.selectCount(examWrapper);
        if (count != null && count > 0) {
            throw new RuntimeException("相同时间的考试安排已存在");
        }

        if (exam.getStartTime().isBefore(LocalDateTime.now()) && !isUpdate) {
            throw new RuntimeException("新增考试时，开始时间不能早于当前时间");
        }
    }
}