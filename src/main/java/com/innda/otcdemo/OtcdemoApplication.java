package com.innda.otcdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.innda.otcdemo.dao.mapper")
public class OtcdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtcdemoApplication.class, args);
    }

}
