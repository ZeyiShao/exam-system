package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.common.Result;
import com.example.examsystem.entity.Course;
import com.example.examsystem.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody Course course) {
        courseService.add(course);
        return Result.success("新增课程成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return Result.success("删除课程成功", null);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Course course) {
        courseService.update(course);
        return Result.success("修改课程成功", null);
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Integer id) {
        return Result.success("查询成功", courseService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<Course>> list() {
        return Result.success("查询成功", courseService.list());
    }

    @GetMapping("/page")
    public Result<IPage<Course>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String courseName) {

        return Result.success("查询成功",
                courseService.page(pageNum, pageSize, courseName));
    }
}