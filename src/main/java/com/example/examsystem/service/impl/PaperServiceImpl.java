package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.common.BusinessException;
import com.example.examsystem.entity.Paper;
import com.example.examsystem.entity.PaperQuestion;
import com.example.examsystem.entity.Question;
import com.example.examsystem.mapper.PaperMapper;
import com.example.examsystem.mapper.PaperQuestionMapper;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.service.PaperService;
import com.example.examsystem.vo.PaperAddVO;
import com.example.examsystem.vo.PaperDetailVO;
import com.example.examsystem.vo.PaperQuestionDetailVO;
import com.example.examsystem.vo.PaperQuestionVO;
import com.example.examsystem.vo.PaperRandomAddVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PaperServiceImpl implements PaperService {

    private final PaperMapper paperMapper;
    private final PaperQuestionMapper paperQuestionMapper;
    private final QuestionMapper questionMapper;

    public PaperServiceImpl(PaperMapper paperMapper,
                            PaperQuestionMapper paperQuestionMapper,
                            QuestionMapper questionMapper) {
        this.paperMapper = paperMapper;
        this.paperQuestionMapper = paperQuestionMapper;
        this.questionMapper = questionMapper;
    }

    @Override
    @Transactional
    public void add(PaperAddVO paperAddVO) {
        validateManualPaper(paperAddVO, false);

        Paper paper = new Paper();
        paper.setPaperName(paperAddVO.getPaperName().trim());
        paper.setTotalScore(paperAddVO.getTotalScore());
        paper.setDuration(paperAddVO.getDuration());
        paper.setPassScore(paperAddVO.getPassScore());
        paper.setCreateUser(paperAddVO.getCreateUser());

        paperMapper.insert(paper);

        saveManualPaperQuestions(paper.getId(), paperAddVO.getQuestionList());
    }

    @Override
    @Transactional
    public void update(PaperAddVO paperAddVO) {
        validateManualPaper(paperAddVO, true);

        Paper paper = new Paper();
        paper.setId(paperAddVO.getId());
        paper.setPaperName(paperAddVO.getPaperName().trim());
        paper.setTotalScore(paperAddVO.getTotalScore());
        paper.setDuration(paperAddVO.getDuration());
        paper.setPassScore(paperAddVO.getPassScore());
        paper.setCreateUser(paperAddVO.getCreateUser());

        paperMapper.updateById(paper);

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, paperAddVO.getId());
        paperQuestionMapper.delete(wrapper);

        saveManualPaperQuestions(paperAddVO.getId(), paperAddVO.getQuestionList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            throw new BusinessException("试卷ID不能为空");
        }

        Paper paper = paperMapper.selectById(id);
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, id);
        paperQuestionMapper.delete(wrapper);

        paperMapper.deleteById(id);
    }

    @Override
    public PaperDetailVO getById(Integer id) {
        if (id == null) {
            throw new BusinessException("试卷ID不能为空");
        }

        Paper paper = paperMapper.selectById(id);
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }

        PaperDetailVO detailVO = new PaperDetailVO();
        detailVO.setId(paper.getId());
        detailVO.setPaperName(paper.getPaperName());
        detailVO.setTotalScore(paper.getTotalScore());
        detailVO.setDuration(paper.getDuration());
        detailVO.setPassScore(paper.getPassScore());
        detailVO.setCreateUser(paper.getCreateUser());
        detailVO.setCreateTime(paper.getCreateTime());

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, id);
        List<PaperQuestion> paperQuestionList = paperQuestionMapper.selectList(wrapper);

        List<PaperQuestionDetailVO> questionList = new ArrayList<>();

        for (PaperQuestion paperQuestion : paperQuestionList) {
            Question question = questionMapper.selectById(paperQuestion.getQuestionId());
            if (question != null) {
                PaperQuestionDetailVO questionVO = new PaperQuestionDetailVO();
                questionVO.setQuestionId(question.getId());
                questionVO.setQuestionText(question.getQuestionText());
                questionVO.setOptionA(question.getOptionA());
                questionVO.setOptionB(question.getOptionB());
                questionVO.setOptionC(question.getOptionC());
                questionVO.setOptionD(question.getOptionD());
                questionVO.setCorrectAnswer(question.getCorrectAnswer());
                questionVO.setQuestionType(question.getQuestionType());
                questionVO.setDifficulty(question.getDifficulty());
                questionVO.setCourse(question.getCourse());
                questionVO.setScore(paperQuestion.getScore());

                questionList.add(questionVO);
            }
        }

        detailVO.setQuestionList(questionList);
        return detailVO;
    }

    @Override
    public List<Paper> list() {
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Paper::getId);
        return paperMapper.selectList(wrapper);
    }

    @Override
    public IPage<Paper> page(Integer pageNum, Integer pageSize, String paperName) {
        Page<Paper> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(paperName)) {
            wrapper.like(Paper::getPaperName, paperName);
        }
        wrapper.orderByDesc(Paper::getId);

        return paperMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public void randomAdd(PaperRandomAddVO randomAddVO) {
        validateRandomPaper(randomAddVO);

        List<Question> singleList = getRandomQuestions(
                "single",
                randomAddVO.getSingleCount(),
                randomAddVO.getCourseId(),
                randomAddVO.getDifficulty()
        );

        List<Question> multipleList = getRandomQuestions(
                "multiple",
                randomAddVO.getMultipleCount(),
                randomAddVO.getCourseId(),
                randomAddVO.getDifficulty()
        );

        List<Question> judgeList = getRandomQuestions(
                "judge",
                randomAddVO.getJudgeCount(),
                randomAddVO.getCourseId(),
                randomAddVO.getDifficulty()
        );

        int totalScore = safeMultiply(randomAddVO.getSingleCount(), randomAddVO.getSingleScore())
                + safeMultiply(randomAddVO.getMultipleCount(), randomAddVO.getMultipleScore())
                + safeMultiply(randomAddVO.getJudgeCount(), randomAddVO.getJudgeScore());

        Paper paper = new Paper();
        paper.setPaperName(randomAddVO.getPaperName().trim());
        paper.setDuration(randomAddVO.getDuration());
        paper.setPassScore(randomAddVO.getPassScore());
        paper.setCreateUser(randomAddVO.getCreateUser());
        paper.setTotalScore(totalScore);

        paperMapper.insert(paper);

        savePaperQuestions(paper.getId(), singleList, randomAddVO.getSingleScore());
        savePaperQuestions(paper.getId(), multipleList, randomAddVO.getMultipleScore());
        savePaperQuestions(paper.getId(), judgeList, randomAddVO.getJudgeScore());
    }

    private void validateManualPaper(PaperAddVO paperAddVO, boolean isUpdate) {
        if (paperAddVO == null) {
            throw new BusinessException("试卷信息不能为空");
        }

        if (isUpdate) {
            if (paperAddVO.getId() == null) {
                throw new BusinessException("试卷ID不能为空");
            }

            Paper oldPaper = paperMapper.selectById(paperAddVO.getId());
            if (oldPaper == null) {
                throw new BusinessException("试卷不存在");
            }
        }

        if (!StringUtils.hasText(paperAddVO.getPaperName())) {
            throw new BusinessException("试卷名称不能为空");
        }

        if (paperAddVO.getDuration() == null || paperAddVO.getDuration() <= 0) {
            throw new BusinessException("试卷时长必须大于0");
        }

        if (paperAddVO.getTotalScore() == null || paperAddVO.getTotalScore() <= 0) {
            throw new BusinessException("试卷总分必须大于0");
        }

        if (paperAddVO.getPassScore() == null || paperAddVO.getPassScore() <= 0) {
            throw new BusinessException("及格分数必须大于0");
        }

        if (paperAddVO.getPassScore() > paperAddVO.getTotalScore()) {
            throw new BusinessException("及格分数不能大于试卷总分");
        }

        if (CollectionUtils.isEmpty(paperAddVO.getQuestionList())) {
            throw new BusinessException("试卷至少需要包含一道题目");
        }

        Set<Integer> questionIdSet = new HashSet<>();
        int scoreSum = 0;

        for (PaperQuestionVO item : paperAddVO.getQuestionList()) {
            if (item == null) {
                throw new BusinessException("题目信息不能为空");
            }

            if (item.getQuestionId() == null) {
                throw new BusinessException("题目ID不能为空");
            }

            if (item.getScore() == null || item.getScore() <= 0) {
                throw new BusinessException("题目分值必须大于0");
            }

            if (!questionIdSet.add(item.getQuestionId())) {
                throw new BusinessException("同一张试卷中不能重复添加同一道题目");
            }

            Question question = questionMapper.selectById(item.getQuestionId());
            if (question == null) {
                throw new BusinessException("题目不存在，题目ID：" + item.getQuestionId());
            }

            scoreSum += item.getScore();
        }

        if (scoreSum != paperAddVO.getTotalScore()) {
            throw new BusinessException("题目分值总和必须等于试卷总分，当前题目总分为：" + scoreSum);
        }
    }

    private void saveManualPaperQuestions(Integer paperId, List<PaperQuestionVO> questionList) {
        for (PaperQuestionVO item : questionList) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperId);
            paperQuestion.setQuestionId(item.getQuestionId());
            paperQuestion.setScore(item.getScore());
            paperQuestionMapper.insert(paperQuestion);
        }
    }

    private void validateRandomPaper(PaperRandomAddVO randomAddVO) {
        if (randomAddVO == null) {
            throw new BusinessException("随机组卷参数不能为空");
        }

        if (!StringUtils.hasText(randomAddVO.getPaperName())) {
            throw new BusinessException("试卷名称不能为空");
        }

        if (randomAddVO.getDuration() == null || randomAddVO.getDuration() <= 0) {
            throw new BusinessException("试卷时长必须大于0");
        }

        if (randomAddVO.getPassScore() == null || randomAddVO.getPassScore() <= 0) {
            throw new BusinessException("及格分数必须大于0");
        }

        int singleCount = safeNumber(randomAddVO.getSingleCount());
        int multipleCount = safeNumber(randomAddVO.getMultipleCount());
        int judgeCount = safeNumber(randomAddVO.getJudgeCount());

        int singleScore = safeNumber(randomAddVO.getSingleScore());
        int multipleScore = safeNumber(randomAddVO.getMultipleScore());
        int judgeScore = safeNumber(randomAddVO.getJudgeScore());

        int totalCount = singleCount + multipleCount + judgeCount;
        if (totalCount <= 0) {
            throw new BusinessException("请至少设置一种题型的题目数量");
        }

        if (singleCount > 0 && singleScore <= 0) {
            throw new BusinessException("单选题数量大于0时，每题分值必须大于0");
        }

        if (multipleCount > 0 && multipleScore <= 0) {
            throw new BusinessException("多选题数量大于0时，每题分值必须大于0");
        }

        if (judgeCount > 0 && judgeScore <= 0) {
            throw new BusinessException("判断题数量大于0时，每题分值必须大于0");
        }

        int totalScore = safeMultiply(singleCount, singleScore)
                + safeMultiply(multipleCount, multipleScore)
                + safeMultiply(judgeCount, judgeScore);

        if (totalScore <= 0) {
            throw new BusinessException("试卷总分必须大于0");
        }

        if (randomAddVO.getPassScore() > totalScore) {
            throw new BusinessException("及格分数不能大于试卷总分");
        }

        if (randomAddVO.getDifficulty() != null) {
            int difficulty = randomAddVO.getDifficulty();
            if (difficulty < 1 || difficulty > 3) {
                throw new BusinessException("难度只能是1、2、3");
            }
        }
    }

    private List<Question> getRandomQuestions(String questionType,
                                              Integer count,
                                              Integer courseId,
                                              Integer difficulty) {
        if (count == null || count <= 0) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getQuestionType, questionType);

        if (courseId != null) {
            wrapper.eq(Question::getCourseId, courseId);
        }

        if (difficulty != null) {
            wrapper.eq(Question::getDifficulty, difficulty);
        }

        List<Question> questionList = questionMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(questionList) || questionList.size() < count) {
            throw new BusinessException(buildQuestionNotEnoughMessage(
                    questionType,
                    count,
                    courseId,
                    difficulty,
                    questionList == null ? 0 : questionList.size()
            ));
        }

        Collections.shuffle(questionList);
        return questionList.subList(0, count);
    }

    private String buildQuestionNotEnoughMessage(String questionType,
                                                 Integer needCount,
                                                 Integer courseId,
                                                 Integer difficulty,
                                                 Integer actualCount) {
        StringBuilder message = new StringBuilder();
        message.append(getQuestionTypeText(questionType))
                .append("数量不足，无法随机组卷");

        message.append("。需要")
                .append(needCount)
                .append("道，当前符合条件的题目有")
                .append(actualCount)
                .append("道");

        if (courseId != null) {
            message.append("，课程ID：").append(courseId);
        }

        if (difficulty != null) {
            message.append("，难度条件：").append(difficulty);
        }

        return message.toString();
    }

    private String getQuestionTypeText(String questionType) {
        if ("single".equals(questionType)) {
            return "单选题";
        }
        if ("multiple".equals(questionType)) {
            return "多选题";
        }
        if ("judge".equals(questionType)) {
            return "判断题";
        }
        return questionType;
    }

    private int safeNumber(Integer value) {
        return value == null ? 0 : value;
    }

    private int safeMultiply(Integer count, Integer score) {
        return safeNumber(count) * safeNumber(score);
    }

    private void savePaperQuestions(Integer paperId, List<Question> questionList, Integer score) {
        if (CollectionUtils.isEmpty(questionList)) {
            return;
        }

        for (Question question : questionList) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperId);
            paperQuestion.setQuestionId(question.getId());
            paperQuestion.setScore(score);
            paperQuestionMapper.insert(paperQuestion);
        }
    }
}