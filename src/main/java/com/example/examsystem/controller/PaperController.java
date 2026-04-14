package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.entity.Paper;
import com.example.examsystem.service.PaperService;
import com.example.examsystem.vo.PaperAddVO;
import com.example.examsystem.vo.PaperDetailVO;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.vo.PaperRandomAddVO;

import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody PaperAddVO paperAddVO) {
        paperService.add(paperAddVO);
        return Result.success("新增试卷成功", null);
    }

    @GetMapping("/{id}")
    public Result<PaperDetailVO> getById(@PathVariable Integer id) {
        return Result.success("查询成功", paperService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<Paper>> list() {
        return Result.success("查询成功", paperService.list());
    }

    @GetMapping("/page")
    public Result<IPage<Paper>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String paperName) {

        return Result.success("查询成功",
                paperService.page(pageNum, pageSize, paperName));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        paperService.delete(id);
        return Result.success("删除成功", null);
    }

    @PutMapping
    public Result<Void> update(@RequestBody PaperAddVO paperAddVO) {
        paperService.update(paperAddVO);
        return Result.success("修改成功", null);
    }

    @PostMapping("/random")
    public Result<Void> randomAdd(@RequestBody PaperRandomAddVO randomAddVO) {
        paperService.randomAdd(randomAddVO);
        return Result.success("随机组卷成功", null);
    }
}