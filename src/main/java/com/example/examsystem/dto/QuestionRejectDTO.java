package com.example.examsystem.dto;

import lombok.Data;

@Data
public class QuestionRejectDTO {

    private Long auditUser;

    private String rejectReason;
}