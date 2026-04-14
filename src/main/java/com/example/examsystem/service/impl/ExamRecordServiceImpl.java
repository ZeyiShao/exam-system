package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.examsystem.dto.ExamStartDTO;
import com.example.examsystem.dto.ExamSubmitAnswerDTO;
import com.example.examsystem.dto.ExamSubmitDTO;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.entity.ExamAnswer;
import com.example.examsystem.entity.ExamRecord;
import com.example.examsystem.entity.Paper;
import com.example.examsystem.entity.PaperQuestion;
import com.example.examsystem.entity.Question;
import com.example.examsystem.entity.StudentClass;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.mapper.ExamAnswerMapper;
import com.example.examsystem.mapper.ExamMapper;
import com.example.examsystem.mapper.ExamRecordMapper;
import com.example.examsystem.mapper.PaperMapper;
import com.example.examsystem.mapper.PaperQuestionMapper;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.mapper.StudentClassMapper;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.service.ExamRecordService;
import com.example.examsystem.vo.ExamSubmitResultVO;
import com.example.examsystem.vo.StudentExamDetailVO;
import com.example.examsystem.vo.StudentExamQuestionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamRecordServiceImpl implements ExamRecordService {

    private final ExamRecordMapper examRecordMapper;
    private final ExamAnswerMapper examAnswerMapper;
    private final ExamMapper examMapper;
    private final PaperMapper paperMapper;
    private final PaperQuestionMapper paperQuestionMapper;
    private final QuestionMapper questionMapper;
    private final SysUserMapper sysUserMapper;
    private final StudentClassMapper studentClassMapper;

    public ExamRecordServiceImpl(ExamRecordMapper examRecordMapper,
                                 ExamAnswerMapper examAnswerMapper,
                                 ExamMapper examMapper,
                                 PaperMapper paperMapper,
                                 PaperQuestionMapper paperQuestionMapper,
                                 QuestionMapper questionMapper,
                                 SysUserMapper sysUserMapper,
                                 StudentClassMapper studentClassMapper) {
        this.examRecordMapper = examRecordMapper;
        this.examAnswerMapper = examAnswerMapper;
        this.examMapper = examMapper;
        this.paperMapper = paperMapper;
        this.paperQuestionMapper = paperQuestionMapper;
        this.questionMapper = questionMapper;
        this.sysUserMapper = sysUserMapper;
        this.studentClassMapper = studentClassMapper;
    }

    @Override
    @Transactional
    public Integer startExam(ExamStartDTO dto) {
        if (dto.getExamId() == null) {
            throw new RuntimeException("考试ID不能为空");
        }
        if (dto.getStudentId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }

        Exam exam = examMapper.selectById(dto.getExamId());
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }

        SysUser student = sysUserMapper.selectById(dto.getStudentId());
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        if (!"STUDENT".equals(student.getRole())) {
            throw new RuntimeException("该用户不是学生");
        }

        LambdaQueryWrapper<StudentClass> studentClassWrapper = new LambdaQueryWrapper<>();
        studentClassWrapper.eq(StudentClass::getStudentId, dto.getStudentId())
                .eq(StudentClass::getClassId, exam.getClassId());

        Long studentClassCount = studentClassMapper.selectCount(studentClassWrapper);
        if (studentClassCount == null || studentClassCount == 0) {
            throw new RuntimeException("该学生不属于本次考试班级，不能参加考试");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new RuntimeException("考试未开始");
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new RuntimeException("考试已结束");
        }

        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getExamId, dto.getExamId())
                .eq(ExamRecord::getStudentId, dto.getStudentId())
                .orderByDesc(ExamRecord::getId);

        List<ExamRecord> recordList = examRecordMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(recordList)) {
            for (ExamRecord record : recordList) {
                if ("FINISHED".equals(record.getStatus())) {
                    throw new RuntimeException("该学生已完成本次考试");
                }
                if ("DOING".equals(record.getStatus())) {
                    return record.getId();
                }
            }
        }

        ExamRecord examRecord = new ExamRecord();
        examRecord.setExamId(dto.getExamId());
        examRecord.setStudentId(dto.getStudentId());
        examRecord.setScore(0);
        examRecord.setStartTime(now);
        examRecord.setStatus("DOING");
        examRecord.setIsPass(false);

        examRecordMapper.insert(examRecord);
        return examRecord.getId();
    }

    @Override
    public StudentExamDetailVO getExamDetail(Integer recordId) {
        ExamRecord examRecord = examRecordMapper.selectById(recordId);
        if (examRecord == null) {
            throw new RuntimeException("考试记录不存在");
        }

        Exam exam = examMapper.selectById(examRecord.getExamId());
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }

        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw new RuntimeException("试卷不存在");
        }

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, paper.getId())
                .orderByAsc(PaperQuestion::getId);

        List<PaperQuestion> paperQuestionList = paperQuestionMapper.selectList(wrapper);

        List<StudentExamQuestionVO> questionList = new ArrayList<>();
        for (PaperQuestion paperQuestion : paperQuestionList) {
            Question question = questionMapper.selectById(paperQuestion.getQuestionId());
            if (question != null) {
                StudentExamQuestionVO vo = new StudentExamQuestionVO();
                vo.setQuestionId(question.getId());
                vo.setQuestionText(question.getQuestionText());
                vo.setOptionA(question.getOptionA());
                vo.setOptionB(question.getOptionB());
                vo.setOptionC(question.getOptionC());
                vo.setOptionD(question.getOptionD());
                vo.setQuestionType(question.getQuestionType());
                vo.setScore(paperQuestion.getScore());
                questionList.add(vo);
            }
        }

        StudentExamDetailVO detailVO = new StudentExamDetailVO();
        detailVO.setRecordId(examRecord.getId());
        detailVO.setExamId(exam.getId());
        detailVO.setExamName(exam.getExamName());
        detailVO.setPaperId(exam.getPaperId());
        detailVO.setDuration(exam.getDuration());
        detailVO.setStartTime(exam.getStartTime());
        detailVO.setEndTime(exam.getEndTime());
        detailVO.setQuestionList(questionList);

        return detailVO;
    }

    @Override
    @Transactional
    public ExamSubmitResultVO submitExam(ExamSubmitDTO dto) {
        if (dto.getRecordId() == null) {
            throw new RuntimeException("考试记录ID不能为空");
        }

        ExamRecord examRecord = examRecordMapper.selectById(dto.getRecordId());
        if (examRecord == null) {
            throw new RuntimeException("考试记录不存在");
        }

        if ("FINISHED".equals(examRecord.getStatus())) {
            throw new RuntimeException("该考试已提交");
        }

        Exam exam = examMapper.selectById(examRecord.getExamId());
        if (exam == null) {
            throw new RuntimeException("考试不存在");
        }

        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper == null) {
            throw new RuntimeException("试卷不存在");
        }

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, paper.getId());

        List<PaperQuestion> paperQuestionList = paperQuestionMapper.selectList(wrapper);

        Map<Integer, String> answerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(dto.getAnswerList())) {
            for (ExamSubmitAnswerDTO answerDTO : dto.getAnswerList()) {
                if (answerDTO.getQuestionId() != null) {
                    answerMap.put(answerDTO.getQuestionId(), answerDTO.getUserAnswer());
                }
            }
        }

        LambdaQueryWrapper<ExamAnswer> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ExamAnswer::getRecordId, dto.getRecordId());
        examAnswerMapper.delete(deleteWrapper);

        int totalScore = 0;

        for (PaperQuestion paperQuestion : paperQuestionList) {
            Question question = questionMapper.selectById(paperQuestion.getQuestionId());
            if (question == null) {
                continue;
            }

            String userAnswer = answerMap.get(question.getId());
            String correctAnswer = question.getCorrectAnswer();

            boolean isCorrect = compareAnswer(userAnswer, correctAnswer, question.getQuestionType());
            int score = isCorrect ? paperQuestion.getScore() : 0;

            ExamAnswer examAnswer = new ExamAnswer();
            examAnswer.setRecordId(dto.getRecordId());
            examAnswer.setQuestionId(question.getId());
            examAnswer.setUserAnswer(userAnswer);
            examAnswer.setCorrectAnswer(correctAnswer);
            examAnswer.setIsCorrect(isCorrect);
            examAnswer.setScore(score);
            examAnswerMapper.insert(examAnswer);

            totalScore += score;
        }

        boolean isPass = totalScore >= paper.getPassScore();

        examRecord.setScore(totalScore);
        examRecord.setSubmitTime(LocalDateTime.now());
        examRecord.setStatus("FINISHED");
        examRecord.setIsPass(isPass);
        examRecordMapper.updateById(examRecord);

        ExamSubmitResultVO resultVO = new ExamSubmitResultVO();
        resultVO.setRecordId(examRecord.getId());
        resultVO.setScore(totalScore);
        resultVO.setIsPass(isPass);
        resultVO.setStatus("FINISHED");

        return resultVO;
    }

    @Override
    public ExamRecord getById(Integer id) {
        ExamRecord examRecord = examRecordMapper.selectById(id);
        if (examRecord == null) {
            throw new RuntimeException("考试记录不存在");
        }
        return examRecord;
    }

    @Override
    public List<ExamRecord> getByStudentId(Long studentId) {
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getStudentId, studentId)
                .orderByDesc(ExamRecord::getId);
        return examRecordMapper.selectList(wrapper);
    }

    @Override
    public List<ExamRecord> getByExamId(Integer examId) {
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getExamId, examId)
                .orderByDesc(ExamRecord::getId);
        return examRecordMapper.selectList(wrapper);
    }

    private boolean compareAnswer(String userAnswer, String correctAnswer, String questionType) {
        String normalizedUserAnswer = normalizeAnswer(userAnswer, questionType);
        String normalizedCorrectAnswer = normalizeAnswer(correctAnswer, questionType);
        return normalizedUserAnswer.equals(normalizedCorrectAnswer);
    }

    private String normalizeAnswer(String answer, String questionType) {
        if (answer == null) {
            return "";
        }

        String result = answer.trim().replace(" ", "");

        if ("multiple".equals(questionType)) {
            String[] arr = result.split(",");
            List<String> list = new ArrayList<>();
            for (String item : arr) {
                if (item != null && !item.trim().isEmpty()) {
                    list.add(item.trim().toUpperCase());
                }
            }
            list.sort(String::compareTo);
            return String.join(",", list);
        }

        if ("judge".equals(questionType)) {
            return result.toLowerCase();
        }

        return result.toUpperCase();
    }
}