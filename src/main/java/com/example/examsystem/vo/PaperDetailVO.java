package com.example.examsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaperDetailVO {

    private Integer id;
    private String paperName;
    private Integer totalScore;
    private Integer duration;
    private Integer passScore;
    private Integer createUser;
    private LocalDateTime createTime;

    /**
     * 试卷状态：PENDING 待审核 / APPROVED 已通过 / REJECTED 已驳回
     */
    private String status;

    /**
     * 审核人ID
     */
    private Long auditUser;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 组卷方式：MANUAL 手动组卷 / RANDOM 随机组卷
     */
    private String paperType;

    private List<PaperQuestionDetailVO> questionList;
}