package com.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.common.Result;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.service.ClassInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassInfoController {

    private final ClassInfoService classInfoService;

    public ClassInfoController(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody ClassInfo classInfo) {
        classInfoService.add(classInfo);
        return Result.success("新增班级成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        classInfoService.delete(id);
        return Result.success("删除班级成功", null);
    }

    @PutMapping
    public Result<Void> update(@RequestBody ClassInfo classInfo) {
        classInfoService.update(classInfo);
        return Result.success("修改班级成功", null);
    }

    @GetMapping("/{id}")
    public Result<ClassInfo> getById(@PathVariable Integer id) {
        return Result.success("查询成功", classInfoService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<ClassInfo>> list() {
        return Result.success("查询成功", classInfoService.list());
    }

    @GetMapping("/page")
    public Result<IPage<ClassInfo>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String className) {

        return Result.success("查询成功",
                classInfoService.page(pageNum, pageSize, className));
    }
}