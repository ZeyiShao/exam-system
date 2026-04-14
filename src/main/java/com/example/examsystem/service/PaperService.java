package com.example.examsystem.service;

import com.example.examsystem.entity.Paper;
import com.example.examsystem.vo.PaperAddVO;
import com.example.examsystem.vo.PaperDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.examsystem.vo.PaperRandomAddVO;

import java.util.List;

public interface PaperService {

    void add(PaperAddVO paperAddVO);

    PaperDetailVO getById(Integer id);

    List<Paper> list();

    IPage<Paper> page(Integer pageNum, Integer pageSize, String paperName);

    void delete(Integer id);

    void update(PaperAddVO paperAddVO);

    void randomAdd(PaperRandomAddVO randomAddVO);
}