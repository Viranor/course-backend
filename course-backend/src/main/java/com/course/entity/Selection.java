package com.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "selection", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id", "semester"})
})
@Data
public class Selection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;  // 选课学生

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // 所选课程

    @Column(nullable = false)
    private String semester;  // 学期

    @Column(name = "select_time", nullable = false)
    private LocalDateTime selectTime = LocalDateTime.now();  // 选课时间

    @Column(nullable = false)
    private String status = "selected";  // 状态: selected-已选, dropped-已退

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}