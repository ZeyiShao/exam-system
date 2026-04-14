package com.example.examsystem.dto;

import lombok.Data;

@Data
public class AddUserDTO {
    private String username;
    private String password;
    private String realName;
    private String role;
}