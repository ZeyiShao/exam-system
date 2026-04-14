package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.dto.ClassCourseTeacherAddDTO;
import com.example.examsystem.service.ClassCourseTeacherService;
import com.example.examsystem.vo.ClassCourseTeacherVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classCourseTeacher")
public class ClassCourseTeacherController {

    private final ClassCourseTeacherService classCourseTeacherService;

    public ClassCourseTeacherController(ClassCourseTeacherService classCourseTeacherService) {
        this.classCourseTeacherService = classCourseTeacherService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody ClassCourseTeacherAddDTO dto) {
        classCourseTeacherService.add(dto.getClassId(), dto.getCourseId(), dto.getTeacherId());
        return Result.success("班级课程教师关系新增成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        classCourseTeacherService.delete(id);
        return Result.success("删除班级课程教师关系成功", null);
    }

    @GetMapping("/list")
    public Result<List<ClassCourseTeacherVO>> list() {
        return Result.success("查询成功", classCourseTeacherService.list());
    }

    @GetMapping("/class/{classId}")
    public Result<List<ClassCourseTeacherVO>> getByClassId(@PathVariable Integer classId) {
        return Result.success("查询成功", classCourseTeacherService.getByClassId(classId));
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<ClassCourseTeacherVO>> getByTeacherId(@PathVariable Long teacherId) {
        return Result.success("查询成功", classCourseTeacherService.getByTeacherId(teacherId));
    }

    @GetMapping("/course/{courseId}")
    public Result<List<ClassCourseTeacherVO>> getByCourseId(@PathVariable Integer courseId) {
        return Result.success("查询成功", classCourseTeacherService.getByCourseId(courseId));
    }
}