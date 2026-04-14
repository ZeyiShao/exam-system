package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {
}