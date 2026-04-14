package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.common.Result;
import com.example.examsystem.entity.Exam;
import com.example.examsystem.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody Exam exam) {
        examService.add(exam);
        return Result.success("新增考试成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        examService.delete(id);
        return Result.success("删除考试成功", null);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Exam exam) {
        examService.update(exam);
        return Result.success("修改考试成功", null);
    }

    @GetMapping("/{id}")
    public Result<Exam> getById(@PathVariable Integer id) {
        return Result.success("查询成功", examService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<Exam>> list() {
        return Result.success("查询成功", examService.list());
    }

    @GetMapping("/page")
    public Result<IPage<Exam>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String examName,
            @RequestParam(required = false) String status) {

        return Result.success("查询成功",
                examService.page(pageNum, pageSize, examName, status));
    }
}