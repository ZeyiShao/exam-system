package com.example.examsystem.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
}