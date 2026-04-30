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
import java.util.List;

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
        Paper paper = new Paper();
        paper.setPaperName(paperAddVO.getPaperName());
        paper.setTotalScore(paperAddVO.getTotalScore());
        paper.setDuration(paperAddVO.getDuration());
        paper.setPassScore(paperAddVO.getPassScore());
        paper.setCreateUser(paperAddVO.getCreateUser());

        paperMapper.insert(paper);

        if (paperAddVO.getQuestionList() != null) {
            for (PaperQuestionVO item : paperAddVO.getQuestionList()) {
                PaperQuestion paperQuestion = new PaperQuestion();
                paperQuestion.setPaperId(paper.getId());
                paperQuestion.setQuestionId(item.getQuestionId());
                paperQuestion.setScore(item.getScore());
                paperQuestionMapper.insert(paperQuestion);
            }
        }
    }

    @Override
    @Transactional
    public void update(PaperAddVO paperAddVO) {
        Paper paper = new Paper();
        paper.setId(paperAddVO.getId());
        paper.setPaperName(paperAddVO.getPaperName());
        paper.setTotalScore(paperAddVO.getTotalScore());
        paper.setDuration(paperAddVO.getDuration());
        paper.setPassScore(paperAddVO.getPassScore());
        paper.setCreateUser(paperAddVO.getCreateUser());

        paperMapper.updateById(paper);

        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, paperAddVO.getId());
        paperQuestionMapper.delete(wrapper);

        if (paperAddVO.getQuestionList() != null) {
            for (PaperQuestionVO item : paperAddVO.getQuestionList()) {
                PaperQuestion pq = new PaperQuestion();
                pq.setPaperId(paperAddVO.getId());
                pq.setQuestionId(item.getQuestionId());
                pq.setScore(item.getScore());
                paperQuestionMapper.insert(pq);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, id);
        paperQuestionMapper.delete(wrapper);

        paperMapper.deleteById(id);
    }

    @Override
    public PaperDetailVO getById(Integer id) {
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
        List<Question> singleList = getRandomQuestions("single", randomAddVO.getSingleCount());
        List<Question> multipleList = getRandomQuestions("multiple", randomAddVO.getMultipleCount());
        List<Question> judgeList = getRandomQuestions("judge", randomAddVO.getJudgeCount());

        int totalScore = randomAddVO.getSingleCount() * randomAddVO.getSingleScore()
                + randomAddVO.getMultipleCount() * randomAddVO.getMultipleScore()
                + randomAddVO.getJudgeCount() * randomAddVO.getJudgeScore();

        Paper paper = new Paper();
        paper.setPaperName(randomAddVO.getPaperName());
        paper.setDuration(randomAddVO.getDuration());
        paper.setPassScore(randomAddVO.getPassScore());
        paper.setCreateUser(randomAddVO.getCreateUser());
        paper.setTotalScore(totalScore);

        paperMapper.insert(paper);

        savePaperQuestions(paper.getId(), singleList, randomAddVO.getSingleScore());
        savePaperQuestions(paper.getId(), multipleList, randomAddVO.getMultipleScore());
        savePaperQuestions(paper.getId(), judgeList, randomAddVO.getJudgeScore());
    }

    private List<Question> getRandomQuestions(String questionType, Integer count) {
        if (count == null || count <= 0) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getQuestionType, questionType);

        List<Question> questionList = questionMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(questionList) || questionList.size() < count) {
            throw new BusinessException(questionType + " 题型数量不足，无法随机组卷");
        }

        Collections.shuffle(questionList);
        return questionList.subList(0, count);
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