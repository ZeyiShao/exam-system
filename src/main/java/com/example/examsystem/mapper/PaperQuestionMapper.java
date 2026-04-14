package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {

    @Select("SELECT score FROM paper_question WHERE question_id = #{questionId}")
    Integer getScoreByQuestionId(Integer questionId);

    @Select("select score from paper_question where paper_id = #{paperId} and question_id = #{questionId} limit 1")
    Integer getScoreByPaperIdAndQuestionId(@Param("paperId") Integer paperId,
                                           @Param("questionId") Integer questionId);
}

