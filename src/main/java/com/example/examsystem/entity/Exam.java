package com.example.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam")
public class Exam {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String examName;

    private Integer paperId;

    private Integer classId;

    private Integer courseId;

    private Long teacherId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration;

    /**
            * 考试运行状态：NOT_STARTED / ONGOING / FINISHED / CANCELLED
 */
    private String status;

    /**
     * 审核状态：PENDING / APPROVED / REJECTED
     */
    private String auditStatus;

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

    private LocalDateTime createTime;
}