package com.example.examsystem.dto;

import lombok.Data;

@Data
public class ExamRejectDTO {

    /**
     * 审核人ID
     */
    private Long auditUser;

    /**
     * 驳回原因
     */
    private String rejectReason;
}