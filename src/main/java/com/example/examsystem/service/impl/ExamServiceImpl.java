package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.BusinessException;
import com.example.examsystem.dto.ExamSubmitAnswerDTO;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.ClassCourseTeacher;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.entity.Course;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.ExamAnswer;
import com.example.examsystem.entity.ExamRecord;
import com.example.examsystem.entity.Paper;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.mapper.ClassCourseTeacherMapper;
import com.example.examsystem.mapper.ClassInfoMapper;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.mapper.ExamAnswerMapper;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.mapper.ExamRecordMapper;
import com.example.examsystem.mapper.PaperMapper;
import com.example.examsystem.mapper.PaperQuestionMapper;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;
    private final PaperMapper paperMapper;
    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final SysUserMapper sysUserMapper;
    private final ClassCourseTeacherMapper classCourseTeacherMapper;
    private final ExamRecordMapper examRecordMapper;
    private final QuestionMapper questionMapper;
    private final ExamAnswerMapper examAnswerMapper;
    private final PaperQuestionMapper paperQuestionMapper;

    public ExamServiceImpl(ExamMapper examMapper,
                           PaperMapper paperMapper,
                           ClassInfoMapper classInfoMapper,
                           CourseMapper courseMapper,
                           SysUserMapper sysUserMapper,
                           ClassCourseTeacherMapper classCourseTeacherMapper,
                           ExamRecordMapper examRecordMapper,
                           QuestionMapper questionMapper,
                           ExamAnswerMapper examAnswerMapper,
                           PaperQuestionMapper paperQuestionMapper) {
        this.examMapper = examMapper;
        this.paperMapper = paperMapper;
        this.classInfoMapper = classInfoMapper;
        this.courseMapper = courseMapper;
        this.sysUserMapper = sysUserMapper;
        this.classCourseTeacherMapper = classCourseTeacherMapper;
        this.examRecordMapper = examRecordMapper;
        this.questionMapper = questionMapper;
        this.examAnswerMapper = examAnswerMapper;
        this.paperQuestionMapper = paperQuestionMapper;
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
            throw new BusinessException("考试不存在");
        }
        examMapper.deleteById(id);
    }

    @Override
    public void update(Exam exam) {
        if (exam.getId() == null) {
            throw new BusinessException("考试ID不能为空");
        }

        Exam oldExam = examMapper.selectById(exam.getId());
        if (oldExam == null) {
            throw new BusinessException("考试不存在");
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
            throw new BusinessException("考试不存在");
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
            throw new BusinessException("考试名称不能为空");
        }

        if (exam.getPaperId() == null) {
            throw new BusinessException("试卷ID不能为空");
        }

        if (exam.getClassId() == null) {
            throw new BusinessException("班级ID不能为空");
        }

        if (exam.getCourseId() == null) {
            throw new BusinessException("课程ID不能为空");
        }

        if (exam.getTeacherId() == null) {
            throw new BusinessException("教师ID不能为空");
        }

        if (exam.getStartTime() == null || exam.getEndTime() == null) {
            throw new BusinessException("开始时间和结束时间不能为空");
        }

        if (!exam.getStartTime().isBefore(exam.getEndTime())) {
            throw new BusinessException("开始时间必须早于结束时间");
        }

        if (exam.getDuration() == null || exam.getDuration() <= 0) {
            throw new BusinessException("考试时长必须大于0");
        }

        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }

        ClassInfo classInfo = classInfoMapper.selectById(exam.getClassId());
        if (classInfo == null) {
            throw new BusinessException("班级不存在");
        }

        Course course = courseMapper.selectById(exam.getCourseId());
        if (course == null) {
            throw new BusinessException("课程不存在");
        }

        SysUser teacher = sysUserMapper.selectById(exam.getTeacherId());
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }

        if (!"TEACHER".equals(teacher.getRole())) {
            throw new BusinessException("该用户不是教师");
        }

        LambdaQueryWrapper<ClassCourseTeacher> relationWrapper = new LambdaQueryWrapper<>();
        relationWrapper.eq(ClassCourseTeacher::getClassId, exam.getClassId())
                .eq(ClassCourseTeacher::getCourseId, exam.getCourseId())
                .eq(ClassCourseTeacher::getTeacherId, exam.getTeacherId());

        Long relationCount = classCourseTeacherMapper.selectCount(relationWrapper);
        if (relationCount == null || relationCount == 0) {
            throw new BusinessException("该教师未负责此班级的该课程，不能创建考试");
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
            throw new BusinessException("相同时间的考试安排已存在");
        }

        if (exam.getStartTime().isBefore(LocalDateTime.now()) && !isUpdate) {
            throw new BusinessException("新增考试时，开始时间不能早于当前时间");
        }
    }

    @Override
    public void submitExam(ExamSubmitDTO dto) {
        if (dto == null || dto.getRecordId() == null) {
            throw new BusinessException("考试记录ID不能为空");
        }

        ExamRecord record = examRecordMapper.selectById(dto.getRecordId());
        if (record == null) {
            throw new BusinessException("考试记录不存在");
        }

        if ("FINISHED".equals(record.getStatus())) {
            throw new BusinessException("已经提交过试卷");
        }

        Exam exam = examMapper.selectById(record.getExamId());
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }

        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }

        if (dto.getAnswerList() == null || dto.getAnswerList().isEmpty()) {
            throw new BusinessException("提交答案不能为空");
        }

        int totalScore = 0;

        for (ExamSubmitAnswerDTO answerDTO : dto.getAnswerList()) {
            if (answerDTO.getQuestionId() == null) {
                throw new BusinessException("题目ID不能为空");
            }

            Question question = questionMapper.selectById(answerDTO.getQuestionId());
            if (question == null) {
                throw new BusinessException("题目不存在");
            }

            String userAnswer = answerDTO.getUserAnswer();
            String correctAnswer = question.getCorrectAnswer();

            boolean isCorrect = false;
            int score = 0;

            if (StringUtils.hasText(userAnswer)) {
                if ("single".equals(question.getQuestionType()) || "judge".equals(question.getQuestionType())) {
                    if (correctAnswer.equalsIgnoreCase(userAnswer.trim())) {
                        isCorrect = true;
                    }
                } else if ("multiple".equals(question.getQuestionType())) {
                    Set<String> userSet = new HashSet<>();
                    for (String s : userAnswer.split(",")) {
                        if (StringUtils.hasText(s)) {
                            userSet.add(s.trim().toUpperCase());
                        }
                    }

                    Set<String> correctSet = new HashSet<>();
                    for (String s : correctAnswer.split(",")) {
                        if (StringUtils.hasText(s)) {
                            correctSet.add(s.trim().toUpperCase());
                        }
                    }

                    if (userSet.equals(correctSet)) {
                        isCorrect = true;
                    }
                }
            }

            Integer questionScore = paperQuestionMapper.getScoreByPaperIdAndQuestionId(exam.getPaperId(), question.getId());
            if (questionScore == null) {
                questionScore = 0;
            }

            if (isCorrect) {
                score = questionScore;
                totalScore += score;
            }

            ExamAnswer examAnswer = new ExamAnswer();
            examAnswer.setRecordId(record.getId());
            examAnswer.setQuestionId(question.getId());
            examAnswer.setUserAnswer(userAnswer);
            examAnswer.setCorrectAnswer(correctAnswer);
            examAnswer.setIsCorrect(isCorrect);
            examAnswer.setScore(score);

            examAnswerMapper.insert(examAnswer);
        }

        record.setScore(totalScore);
        record.setSubmitTime(LocalDateTime.now());
        record.setStatus("FINISHED");
        record.setIsPass(totalScore >= paper.getPassScore());

        examRecordMapper.updateById(record);
    }
}