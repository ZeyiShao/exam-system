package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.dto.TeacherCourseAddDTO;
import com.example.examsystem.service.TeacherCourseService;
import com.example.examsystem.vo.TeacherCourseVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacherCourse")
public class TeacherCourseController {

    private final TeacherCourseService teacherCourseService;

    public TeacherCourseController(TeacherCourseService teacherCourseService) {
        this.teacherCourseService = teacherCourseService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody TeacherCourseAddDTO dto) {
        teacherCourseService.add(dto.getTeacherId(), dto.getCourseId());
        return Result.success("教师分配课程成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        teacherCourseService.delete(id);
        return Result.success("删除教师课程关系成功", null);
    }

    @GetMapping("/list")
    public Result<List<TeacherCourseVO>> list() {
        return Result.success("查询成功", teacherCourseService.list());
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<TeacherCourseVO>> getByTeacherId(@PathVariable Long teacherId) {
        return Result.success("查询成功", teacherCourseService.getByTeacherId(teacherId));
    }

    @GetMapping("/course/{courseId}")
    public Result<List<TeacherCourseVO>> getByCourseId(@PathVariable Integer courseId) {
        return Result.success("查询成功", teacherCourseService.getByCourseId(courseId));
    }
}