package com.example.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.entity.ClassInfo;
import com.example.examsystem.mapper.ClassInfoMapper;
import com.example.examsystem.service.ClassInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    private final ClassInfoMapper classInfoMapper;

    public ClassInfoServiceImpl(ClassInfoMapper classInfoMapper) {
        this.classInfoMapper = classInfoMapper;
    }

    @Override
    public void add(ClassInfo classInfo) {
        classInfoMapper.insert(classInfo);
    }

    @Override
    public void delete(Integer id) {
        classInfoMapper.deleteById(id);
    }

    @Override
    public void update(ClassInfo classInfo) {
        classInfoMapper.updateById(classInfo);
    }

    @Override
    public ClassInfo getById(Integer id) {
        return classInfoMapper.selectById(id);
    }

    @Override
    public List<ClassInfo> list() {
        LambdaQueryWrapper<ClassInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ClassInfo::getId);
        return classInfoMapper.selectList(wrapper);
    }

    @Override
    public IPage<ClassInfo> page(Integer pageNum, Integer pageSize, String className) {
        Page<ClassInfo> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<ClassInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(className)) {
            wrapper.like(ClassInfo::getClassName, className);
        }
        wrapper.orderByDesc(ClassInfo::getId);

        return classInfoMapper.selectPage(page, wrapper);
    }
}