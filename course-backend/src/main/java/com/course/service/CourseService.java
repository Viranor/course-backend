package com.course.service;

import com.course.entity.Course;
import com.course.entity.User;
import com.course.repository.CourseRepository;
import com.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // 创建课程
    public Course createCourse(Course course, Long teacherId) {
        // 验证教师是否存在
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));

        // 验证教师角色
        if (!"teacher".equals(teacher.getRole())) {
            throw new RuntimeException("只有教师可以创建课程");
        }

        // 检查课程编号是否重复
        if (courseRepository.findByCourseCode(course.getCourseCode()).isPresent()) {
            throw new RuntimeException("课程编号已存在");
        }

        // 设置教师ID和默认值
        course.setTeacherId(teacherId);
        course.setCurrentEnrollment(0);  // 对应数据库的selected字段
        course.setStatus("approved");    // 对应数据库的status字段
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());

        return courseRepository.save(course);
    }

    // 获取所有课程
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 根据ID获取课程
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
    }

    // 更新课程信息
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);

        if (courseDetails.getCourseName() != null) {
            course.setCourseName(courseDetails.getCourseName());
        }
        if (courseDetails.getDescription() != null) {
            course.setDescription(courseDetails.getDescription());
        }
        if (courseDetails.getCredit() != null) {
            course.setCredit(courseDetails.getCredit());
        }
        if (courseDetails.getTotalHours() != null) {
            course.setTotalHours(courseDetails.getTotalHours());
        }
        if (courseDetails.getMaxCapacity() != null) {
            course.setMaxCapacity(courseDetails.getMaxCapacity());
        }
        if (courseDetails.getSemester() != null) {
            course.setSemester(courseDetails.getSemester());
        }
        if (courseDetails.getTimeSlot() != null) {
            course.setTimeSlot(courseDetails.getTimeSlot());
        }
        if (courseDetails.getLocation() != null) {
            course.setLocation(courseDetails.getLocation());
        }
        if (courseDetails.getStatus() != null) {
            course.setStatus(courseDetails.getStatus());
        }

        course.setUpdateTime(LocalDateTime.now());
        return courseRepository.save(course);
    }

    // 删除课程（逻辑删除，改为关闭状态）
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        course.setStatus("closed");
        course.setUpdateTime(LocalDateTime.now());
        courseRepository.save(course);
    }

    // 获取教师的所有课程
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    // 搜索课程
    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    // 获取开放选课的课程
    public List<Course> getOpeningCourses() {
        return courseRepository.findByStatus("approved");
    }

    // 根据学期获取课程
    public List<Course> getCoursesBySemester(String semester) {
        return courseRepository.findBySemester(semester);
    }

    // 增加选课人数
    public void incrementEnrollment(Long courseId) {
        Course course = getCourseById(courseId);
        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new RuntimeException("课程人数已满");
        }
        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        course.setUpdateTime(LocalDateTime.now());
        courseRepository.save(course);
    }

    // 减少选课人数
    public void decrementEnrollment(Long courseId) {
        Course course = getCourseById(courseId);
        if (course.getCurrentEnrollment() > 0) {
            course.setCurrentEnrollment(course.getCurrentEnrollment() - 1);
            course.setUpdateTime(LocalDateTime.now());
            courseRepository.save(course);
        }
    }

    // 获取课程统计信息
    public CourseStats getCourseStats() {
        List<Course> allCourses = getAllCourses();
        CourseStats stats = new CourseStats();
        stats.setTotalCourses(allCourses.size());
        stats.setOpeningCourses((int) allCourses.stream()
                .filter(c -> "approved".equals(c.getStatus()))
                .count());
        stats.setTotalEnrollment(allCourses.stream()
                .mapToInt(Course::getCurrentEnrollment)
                .sum());
        stats.setTotalCapacity(allCourses.stream()
                .mapToInt(Course::getMaxCapacity)
                .sum());
        return stats;
    }

    // 课程统计内部类
    public static class CourseStats {
        private int totalCourses;
        private int openingCourses;
        private int totalEnrollment;
        private int totalCapacity;

        // Getter和Setter
        public int getTotalCourses() { return totalCourses; }
        public void setTotalCourses(int totalCourses) { this.totalCourses = totalCourses; }

        public int getOpeningCourses() { return openingCourses; }
        public void setOpeningCourses(int openingCourses) { this.openingCourses = openingCourses; }

        public int getTotalEnrollment() { return totalEnrollment; }
        public void setTotalEnrollment(int totalEnrollment) { this.totalEnrollment = totalEnrollment; }

        public int getTotalCapacity() { return totalCapacity; }
        public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

        public int getAvailableSlots() { return totalCapacity - totalEnrollment; }
    }
}