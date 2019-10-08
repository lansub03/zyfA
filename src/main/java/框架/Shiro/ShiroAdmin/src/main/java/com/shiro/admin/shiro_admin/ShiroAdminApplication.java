package com.shiro.admin.shiro_admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.shiro.amdin.shiro_admin.dao")
@EnableSwagger2//开启Swagger
public class ShiroAdminApplication   {

    public static void main(String[] args) {
        SpringApplication.run(ShiroAdminApplication.class, args);
    }


}
