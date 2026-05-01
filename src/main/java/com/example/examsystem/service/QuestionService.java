package com.example.examsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.entity.Question;

import java.util.List;

public interface QuestionService {

    void add(Question question);

    void delete(Integer id);

    void update(Question question);

    Question getById(Integer id);

    List<Question> list();

    IPage<Question> page(Integer pageNum,
                         Integer pageSize,
                         Integer courseId,
                         String questionType,
                         Integer difficulty,
                         String keyword);
}