package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.entity.Question;
import com.example.examsystem.mapper.QuestionMapper;
import com.example.examsystem.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public void add(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public void delete(Integer id) {
        questionMapper.deleteById(id);
    }

    @Override
    public void update(Question question) {
        questionMapper.updateById(question);
    }

    @Override
    public Question getById(Integer id) {
        return questionMapper.selectById(id);
    }

    @Override
    public List<Question> list() {
        return questionMapper.selectList(null);
    }

    @Override
    public IPage<Question> page(Integer pageNum,
                                Integer pageSize,
                                String course,
                                String questionType,
                                Integer difficulty,
                                String keyword
                                ) {
        Page<Question> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();

        // 课程筛选
        if (StringUtils.hasText(course)) {
            queryWrapper.like(Question::getCourse, course);
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
}