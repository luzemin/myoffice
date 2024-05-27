package com.myoffice.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.myoffice.app.mapper")
public class MyOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOfficeApplication.class, args);
    }

}

