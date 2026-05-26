package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.BusinessException;
import com.example.examsystem.entity.Course;
import com.example.examsystem.entity.Question;
import com.example.examsystem.mapper.CourseMapper;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_REJECTED = "REJECTED";

    private final QuestionMapper questionMapper;
    private final CourseMapper courseMapper;

    public QuestionServiceImpl(QuestionMapper questionMapper,
                               CourseMapper courseMapper) {
        this.questionMapper = questionMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public void add(Question question) {
        validateQuestion(question, false);
        normalizeQuestion(question);

        question.setStatus(STATUS_PENDING);
        question.setAuditUser(null);
        question.setAuditTime(null);
        question.setRejectReason(null);

        questionMapper.insert(question);
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new BusinessException("题目ID不能为空");
        }

        Question oldQuestion = questionMapper.selectById(id);
        if (oldQuestion == null) {
            throw new BusinessException("题目不存在");
        }

        questionMapper.deleteById(id);
    }

    @Override
    public void update(Question question) {
        validateQuestion(question, true);

        Question oldQuestion = questionMapper.selectById(question.getId());
        if (oldQuestion == null) {
            throw new BusinessException("题目不存在");
        }

        normalizeQuestion(question);

        if (STATUS_REJECTED.equals(oldQuestion.getStatus())) {
            question.setStatus(STATUS_PENDING);
            question.setAuditUser(null);
            question.setAuditTime(null);
            question.setRejectReason(null);
        } else if (STATUS_PENDING.equals(oldQuestion.getStatus())) {
            question.setStatus(STATUS_PENDING);
            question.setAuditUser(null);
            question.setAuditTime(null);
            question.setRejectReason(null);
        } else if (STATUS_APPROVED.equals(oldQuestion.getStatus())) {
            throw new BusinessException("已通过审核的题目不能直接修改，请重新新增题目");
        }

        questionMapper.updateById(question);
    }

    @Override
    public Question getById(Integer id) {
        if (id == null) {
            throw new BusinessException("题目ID不能为空");
        }

        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("题目不存在");
        }

        return question;
    }

    @Override
    public List<Question> list() {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, STATUS_APPROVED);
        wrapper.orderByDesc(Question::getId);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public IPage<Question> page(Integer pageNum,
                                Integer pageSize,
                                Integer courseId,
                                String questionType,
                                Integer difficulty,
                                String keyword) {
        Page<Question> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getStatus, STATUS_APPROVED);

        // 课程筛选：方案B使用 courseId，不再使用 course 字符串筛选
        if (courseId != null) {
            queryWrapper.eq(Question::getCourseId, courseId);
        }

        // 题型筛选
        if (StringUtils.hasText(questionType)) {
            queryWrapper.eq(Question::getQuestionType, questionType);
        }

        // 难度筛选
        if (difficulty != null) {
            queryWrapper.eq(Question::getDifficulty, difficulty);
        }

        // 关键字搜索（题目内容）
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Question::getQuestionText, keyword);
        }

        queryWrapper.orderByDesc(Question::getId);

        return questionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Question> reviewPage(Integer pageNum,
                                      Integer pageSize,
                                      Integer courseId,
                                      String questionType,
                                      String keyword) {
        Page<Question> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getStatus, STATUS_PENDING);

        if (courseId != null) {
            queryWrapper.eq(Question::getCourseId, courseId);
        }

        if (StringUtils.hasText(questionType)) {
            queryWrapper.eq(Question::getQuestionType, questionType);
        }

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Question::getQuestionText, keyword);
        }

        queryWrapper.orderByDesc(Question::getId);

        return questionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Question> myPage(Integer pageNum,
                                  Integer pageSize,
                                  Integer createUser,
                                  String status,
                                  Integer courseId,
                                  String questionType,
                                  String keyword) {
        if (createUser == null) {
            throw new BusinessException("创建人ID不能为空");
        }

        Page<Question> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCreateUser, createUser);

        if (StringUtils.hasText(status)) {
            if (!STATUS_PENDING.equals(status) && !STATUS_REJECTED.equals(status)) {
                throw new BusinessException("我的题目只能查询待审核或已驳回状态");
            }
            queryWrapper.eq(Question::getStatus, status);
        } else {
            queryWrapper.in(Question::getStatus, STATUS_PENDING, STATUS_REJECTED);
        }

        if (courseId != null) {
            queryWrapper.eq(Question::getCourseId, courseId);
        }

        if (StringUtils.hasText(questionType)) {
            queryWrapper.eq(Question::getQuestionType, questionType);
        }

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Question::getQuestionText, keyword);
        }

        queryWrapper.orderByDesc(Question::getId);

        return questionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void approve(Integer id, Long auditUser) {
        if (id == null) {
            throw new BusinessException("题目ID不能为空");
        }

        if (auditUser == null) {
            throw new BusinessException("审核人不能为空");
        }

        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("题目不存在");
        }

        if (!STATUS_PENDING.equals(question.getStatus())) {
            throw new BusinessException("只有待审核题目可以审核通过");
        }

        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setStatus(STATUS_APPROVED);
        updateQuestion.setAuditUser(auditUser);
        updateQuestion.setAuditTime(LocalDateTime.now());
        updateQuestion.setRejectReason(null);

        questionMapper.updateById(updateQuestion);
    }

    @Override
    public void reject(Integer id, Long auditUser, String rejectReason) {
        if (id == null) {
            throw new BusinessException("题目ID不能为空");
        }

        if (auditUser == null) {
            throw new BusinessException("审核人不能为空");
        }

        if (!StringUtils.hasText(rejectReason)) {
            throw new BusinessException("驳回原因不能为空");
        }

        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("题目不存在");
        }

        if (!STATUS_PENDING.equals(question.getStatus())) {
            throw new BusinessException("只有待审核题目可以驳回");
        }

        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setStatus(STATUS_REJECTED);
        updateQuestion.setAuditUser(auditUser);
        updateQuestion.setAuditTime(LocalDateTime.now());
        updateQuestion.setRejectReason(rejectReason.trim());

        questionMapper.updateById(updateQuestion);
    }

    private void validateQuestion(Question question, boolean isUpdate) {
        if (question == null) {
            throw new BusinessException("题目信息不能为空");
        }

        if (isUpdate) {
            if (question.getId() == null) {
                throw new BusinessException("题目ID不能为空");
            }

            Question oldQuestion = questionMapper.selectById(question.getId());
            if (oldQuestion == null) {
                throw new BusinessException("题目不存在");
            }
        }

        if (!StringUtils.hasText(question.getQuestionText())) {
            throw new BusinessException("题目内容不能为空");
        }

        if (!StringUtils.hasText(question.getQuestionType())) {
            throw new BusinessException("题型不能为空");
        }

        if (!isValidQuestionType(question.getQuestionType())) {
            throw new BusinessException("题型只能是 single、multiple、judge");
        }

        if (question.getCourseId() == null) {
            throw new BusinessException("课程不能为空");
        }

        Course course = courseMapper.selectById(question.getCourseId());
        if (course == null) {
            throw new BusinessException("课程不存在");
        }

        if (!"ENABLED".equals(course.getStatus())) {
            throw new BusinessException("该课程已禁用，不能添加或修改题目");
        }

        // 根据 courseId 自动回填课程名称，避免前端手输 course 导致不一致
        question.setCourse(course.getCourseName());

        if (question.getDifficulty() == null) {
            throw new BusinessException("难度不能为空");
        }

        if (question.getDifficulty() < 1 || question.getDifficulty() > 3) {
            throw new BusinessException("难度只能是1、2、3");
        }

        if (!StringUtils.hasText(question.getCorrectAnswer())) {
            throw new BusinessException("正确答案不能为空");
        }

        String questionType = question.getQuestionType();

        if ("single".equals(questionType)) {
            validateChoiceOptions(question);
            validateSingleAnswer(question.getCorrectAnswer());
        }

        if ("multiple".equals(questionType)) {
            validateChoiceOptions(question);
            validateMultipleAnswer(question.getCorrectAnswer());
        }

        if ("judge".equals(questionType)) {
            validateJudgeAnswer(question.getCorrectAnswer());
        }
    }

    private void normalizeQuestion(Question question) {
        String questionType = question.getQuestionType().trim();

        question.setQuestionText(question.getQuestionText().trim());
        question.setQuestionType(questionType);

        if (question.getCourse() != null) {
            question.setCourse(question.getCourse().trim());
        }

        if ("single".equals(questionType)) {
            question.setCorrectAnswer(question.getCorrectAnswer().trim().toUpperCase());
        }

        if ("multiple".equals(questionType)) {
            question.setCorrectAnswer(normalizeMultipleAnswer(question.getCorrectAnswer()));
        }

        if ("judge".equals(questionType)) {
            question.setCorrectAnswer(question.getCorrectAnswer().trim().toLowerCase());
            question.setOptionA(null);
            question.setOptionB(null);
            question.setOptionC(null);
            question.setOptionD(null);
        }

        if (question.getOptionA() != null) {
            question.setOptionA(question.getOptionA().trim());
        }

        if (question.getOptionB() != null) {
            question.setOptionB(question.getOptionB().trim());
        }

        if (question.getOptionC() != null) {
            question.setOptionC(question.getOptionC().trim());
        }

        if (question.getOptionD() != null) {
            question.setOptionD(question.getOptionD().trim());
        }
    }

    private boolean isValidQuestionType(String questionType) {
        return "single".equals(questionType)
                || "multiple".equals(questionType)
                || "judge".equals(questionType);
    }

    private void validateChoiceOptions(Question question) {
        if (!StringUtils.hasText(question.getOptionA())
                || !StringUtils.hasText(question.getOptionB())
                || !StringUtils.hasText(question.getOptionC())
                || !StringUtils.hasText(question.getOptionD())) {
            throw new BusinessException("单选题和多选题必须填写A、B、C、D四个选项");
        }
    }

    private void validateSingleAnswer(String answer) {
        String normalizedAnswer = answer.trim().toUpperCase();

        if (!Arrays.asList("A", "B", "C", "D").contains(normalizedAnswer)) {
            throw new BusinessException("单选题正确答案只能是A、B、C、D");
        }
    }

    private void validateMultipleAnswer(String answer) {
        List<String> answerList = parseMultipleAnswer(answer);

        if (answerList.isEmpty()) {
            throw new BusinessException("多选题正确答案不能为空");
        }

        if (answerList.size() < 2) {
            throw new BusinessException("多选题至少应包含两个正确选项");
        }

        for (String item : answerList) {
            if (!Arrays.asList("A", "B", "C", "D").contains(item)) {
                throw new BusinessException("多选题正确答案只能包含A、B、C、D");
            }
        }
    }

    private void validateJudgeAnswer(String answer) {
        String normalizedAnswer = answer.trim().toLowerCase();

        if (!"true".equals(normalizedAnswer) && !"false".equals(normalizedAnswer)) {
            throw new BusinessException("判断题正确答案只能是true或false");
        }
    }

    private List<String> parseMultipleAnswer(String answer) {
        List<String> result = new ArrayList<>();

        if (!StringUtils.hasText(answer)) {
            return result;
        }

        String[] arr = answer.replace("，", ",").split(",");

        for (String item : arr) {
            String normalizedItem = item.trim().toUpperCase();

            if (!StringUtils.hasText(normalizedItem)) {
                continue;
            }

            if (!result.contains(normalizedItem)) {
                result.add(normalizedItem);
            }
        }

        result.sort(String::compareTo);
        return result;
    }

    private String normalizeMultipleAnswer(String answer) {
        List<String> answerList = parseMultipleAnswer(answer);
        return String.join(",", answerList);
    }
}