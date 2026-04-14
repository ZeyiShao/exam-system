package com.example.examsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamSubmitDTO {

    private Integer recordId;

    private List<ExamSubmitAnswerDTO> answerList;
}