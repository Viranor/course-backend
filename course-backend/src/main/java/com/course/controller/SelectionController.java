package com.course.controller;

import com.course.entity.Selection;
import com.course.service.SelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/selections")
public class SelectionController {

    @Autowired
    private SelectionService selectionService;

    // 学生选课
    @PostMapping("/select")
    public Selection selectCourse(@RequestBody Map<String, Object> request) {
        Long studentId = Long.parseLong(request.get("studentId").toString());
        Long courseId = Long.parseLong(request.get("courseId").toString());
        String semester = (String) request.get("semester");
        return selectionService.selectCourse(studentId, courseId, semester);
    }

    // 获取学生的选课记录
    @GetMapping("/student/{studentId}")
    public List<Selection> getStudentSelections(@PathVariable Long studentId) {
        return selectionService.getStudentSelections(studentId);
    }

    // 获取学生当前学期的选课
    @GetMapping("/student/{studentId}/semester/{semester}")
    public List<Selection> getStudentCurrentSelections(
            @PathVariable Long studentId,
            @PathVariable String semester) {
        return selectionService.getStudentCurrentSelections(studentId, semester);
    }

    // 获取课程的选课记录
    @GetMapping("/course/{courseId}")
    public List<Selection> getCourseSelections(@PathVariable Long courseId) {
        return selectionService.getCourseSelections(courseId);
    }

    // 获取学生的课表
    @GetMapping("/student/{studentId}/timetable/{semester}")
    public List<Selection> getStudentTimetable(
            @PathVariable Long studentId,
            @PathVariable String semester) {
        return selectionService.getStudentTimetable(studentId, semester);
    }

    // 获取学生的已选学分
    @GetMapping("/student/{studentId}/credits/{semester}")
    public Integer getStudentCredits(
            @PathVariable Long studentId,
            @PathVariable String semester) {
        return selectionService.getStudentCredits(studentId, semester);
    }

    // 检查学生是否已选某门课程
    @GetMapping("/check")
    public boolean checkCourseSelected(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam String semester) {
        return selectionService.isCourseSelected(studentId, courseId, semester);
    }

    // 学生退课
    @PostMapping("/drop/{selectionId}")
    public Selection dropCourse(@PathVariable Long selectionId) {
        return selectionService.dropCourse(selectionId);
    }
}