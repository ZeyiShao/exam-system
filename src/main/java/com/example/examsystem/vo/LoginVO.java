package com.example.examsystem.vo;

import lombok.Data;

@Data
public class LoginVO {

    private Long id;

    private String username;

    private String realName;

    private String role;
}