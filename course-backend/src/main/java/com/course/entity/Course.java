package com.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 20)
    private String courseCode;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(nullable = false, precision = 3, scale = 1)
    private Integer credit;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "selected")
    private Integer currentEnrollment = 0;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(name = "schedule_time", length = 100)
    private String timeSlot;

    @Column(length = 100)
    private String location;

    @Column(length = 2000)
    private String description;

    @Column(length = 20)
    private String status = "approved";

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}