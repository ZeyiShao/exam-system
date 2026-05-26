package com.example.examsystem.dto;

import lombok.Data;

@Data
public class PaperRejectDTO {

    private Long auditUser;

    private String rejectReason;
}