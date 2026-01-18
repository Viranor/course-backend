package com.course.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    // 获取课程列表
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            // 模拟课程数据
            List<Map<String, Object>> courses = new ArrayList<>();

            Map<String, Object> course1 = new HashMap<>();
            course1.put("id", 1);
            course1.put("name", "Java程序设计");
            course1.put("code", "CS101");
            course1.put("teacher", "张老师");
            course1.put("credit", 3);
            course1.put("capacity", 50);
            course1.put("selected", 30);
            courses.add(course1);

            Map<String, Object> course2 = new HashMap<>();
            course2.put("id", 2);
            course2.put("name", "数据结构");
            course2.put("code", "CS102");
            course2.put("teacher", "李老师");
            course2.put("credit", 4);
            course2.put("capacity", 60);
            course2.put("selected", 45);
            courses.add(course2);

            Map<String, Object> course3 = new HashMap<>();
            course3.put("id", 3);
            course3.put("name", "数据库系统");
            course3.put("code", "CS103");
            course3.put("teacher", "王老师");
            course3.put("credit", 3);
            course3.put("capacity", 40);
            course3.put("selected", 40);
            courses.add(course3);

            // 获取当前用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "匿名用户";

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", courses);
            response.put("total", courses.size());
            response.put("currentUser", username);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取课程列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 根据ID获取课程
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            // 模拟课程数据
            Map<String, Object> course = new HashMap<>();
            course.put("id", id);
            course.put("name", "Java程序设计");
            course.put("code", "CS101");
            course.put("teacher", "张老师");
            course.put("credit", 3);
            course.put("capacity", 50);
            course.put("selected", 30);
            course.put("description", "本课程介绍Java编程语言的基础知识和面向对象编程思想。");
            course.put("schedule", "周一 8:00-10:00, 周三 10:00-12:00");
            course.put("location", "教学楼A201");

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", course);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取课程详情失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}