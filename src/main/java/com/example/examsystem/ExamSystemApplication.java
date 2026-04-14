package com.example.examsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.examsystem.mapper")
public class ExamSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamSystemApplication.class, args);
    }

}
