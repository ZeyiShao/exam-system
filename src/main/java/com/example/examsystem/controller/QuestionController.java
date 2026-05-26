package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.common.Result;
import com.example.examsystem.entity.Question;
import com.example.examsystem.service.QuestionService;
import com.example.examsystem.dto.QuestionRejectDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody Question question) {
        questionService.add(question);
        return Result.success("题目提交成功，等待管理员审核", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        questionService.delete(id);
        return Result.success("删除成功", null);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Question question) {
        questionService.update(question);
        return Result.success("修改成功", null);
    }

    @GetMapping("/{id}")
    public Result<Question> get(@PathVariable Integer id) {
        Question question = questionService.getById(id);
        return Result.success("查询成功", question);
    }

    @GetMapping("/list")
    public Result<List<Question>> list() {
        List<Question> list = questionService.list();
        return Result.success("查询成功", list);
    }

    @GetMapping("/page")
    public Result<IPage<Question>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) String keyword) {

        IPage<Question> page = questionService.page(
                pageNum, pageSize, courseId, questionType, difficulty, keyword);

        return Result.success("查询成功", page);
    }

    @GetMapping("/review/page")
    public Result<IPage<Question>> reviewPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String keyword) {

        IPage<Question> page = questionService.reviewPage(
                pageNum, pageSize, courseId, questionType, keyword);

        return Result.success("查询成功", page);
    }

    @GetMapping("/my/page")
    public Result<IPage<Question>> myPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam Integer createUser,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String keyword) {

        IPage<Question> page = questionService.myPage(
                pageNum, pageSize, createUser, status, courseId, questionType, keyword);

        return Result.success("查询成功", page);
    }

    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Integer id,
                                @RequestParam Long auditUser) {
        questionService.approve(id, auditUser);
        return Result.success("审核通过成功", null);
    }

    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Integer id,
                               @RequestBody QuestionRejectDTO dto) {
        questionService.reject(id, dto.getAuditUser(), dto.getRejectReason());
        return Result.success("题目已驳回", null);
    }
}