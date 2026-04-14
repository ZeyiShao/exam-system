package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.examsystem.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select id, username, password, real_name as realName, role, create_time as createTime from sys_user where username = #{username}")
    SysUser selectByUsername(String username);
}