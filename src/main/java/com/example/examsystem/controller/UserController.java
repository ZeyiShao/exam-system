package com.example.examsystem.controller;

import com.example.examsystem.common.Result;
import com.example.examsystem.dto.AddUserDTO;
import com.example.examsystem.dto.LoginDTO;
import com.example.examsystem.dto.UpdateUserDTO;
import com.example.examsystem.entity.SysUser;
import com.example.examsystem.mapper.SysUserMapper;
import com.example.examsystem.vo.LoginVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserMapper sysUserMapper;

    @GetMapping("/list")
    public Result<List<SysUser>> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role) {

        List<SysUser> list = sysUserMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysUser>()
                        .like(username != null && !username.isEmpty(), "username", username)
                        .eq(role != null && !role.isEmpty(), "role", role)
                        .orderByDesc("create_time")
        );

        // 去掉密码
        for (SysUser user : list) {
            user.setPassword(null);
        }

        return Result.success("查询成功", list);
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {

        SysUser user = sysUserMapper.selectByUsername(loginDTO.getUsername());

        if (user == null) {
            return Result.error("用户名不存在");
        }

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        // 构造返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setRealName(user.getRealName());
        loginVO.setRole(user.getRole());

        return Result.success("登录成功", loginVO);
    }

    @PostMapping("/user/add")
    public Result<String> addUser(@RequestBody AddUserDTO addUserDTO) {
        SysUser existUser = sysUserMapper.selectByUsername(addUserDTO.getUsername());

        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(addUserDTO.getUsername());
        user.setPassword(addUserDTO.getPassword());
        user.setRealName(addUserDTO.getRealName());
        user.setRole(addUserDTO.getRole());

        sysUserMapper.insert(user);

        return Result.success("新增用户成功", null);
    }

    @GetMapping("/user/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        SysUser user = sysUserMapper.selectById(id);

        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setPassword(null);
        return Result.success("查询成功", user);
    }

    @DeleteMapping("/user/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        SysUser user = sysUserMapper.selectById(id);

        if (user == null) {
            return Result.error("用户不存在");
        }

        sysUserMapper.deleteById(id);
        return Result.success("删除用户成功", null);
    }

    @PutMapping("/user/update")
    public Result<String> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        SysUser user = sysUserMapper.selectById(updateUserDTO.getId());

        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setUsername(updateUserDTO.getUsername());
        user.setPassword(updateUserDTO.getPassword());
        user.setRealName(updateUserDTO.getRealName());
        user.setRole(updateUserDTO.getRole());

        sysUserMapper.updateById(user);

        return Result.success("修改用户成功", null);
    }
}