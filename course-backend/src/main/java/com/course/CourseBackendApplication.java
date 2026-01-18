package com.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseBackendApplication.class, args);
        System.out.println("✅ 课程选课系统启动成功！");
        System.out.println("📍 访问地址: http://localhost:8080");
        System.out.println("🔗 测试接口: http://localhost:8080/api/test/hello");
        System.out.println("🔗 根路径: http://localhost:8080/");
    }
}