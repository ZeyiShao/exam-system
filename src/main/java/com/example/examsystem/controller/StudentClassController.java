package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.dto.StudentClassAddDTO;
import com.example.examsystem.service.StudentClassService;
import com.example.examsystem.vo.StudentClassVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentClass")
public class StudentClassController {

    private final StudentClassService studentClassService;

    public StudentClassController(StudentClassService studentClassService) {
        this.studentClassService = studentClassService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody StudentClassAddDTO dto) {
        studentClassService.add(dto.getStudentId(), dto.getClassId());
        return Result.success("学生加入班级成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        studentClassService.delete(id);
        return Result.success("删除学生班级关系成功", null);
    }

    @GetMapping("/list")
    public Result<List<StudentClassVO>> list() {
        return Result.success("查询成功", studentClassService.list());
    }

    @GetMapping("/student/{studentId}")
    public Result<List<StudentClassVO>> getByStudentId(@PathVariable Long studentId) {
        return Result.success("查询成功", studentClassService.getByStudentId(studentId));
    }

    @GetMapping("/class/{classId}")
    public Result<List<StudentClassVO>> getByClassId(@PathVariable Integer classId) {
        return Result.success("查询成功", studentClassService.getByClassId(classId));
    }
}