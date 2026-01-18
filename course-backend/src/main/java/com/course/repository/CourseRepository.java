package com.course.repository;

import com.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 根据课程编号查找
    Optional<Course> findByCourseCode(String courseCode);

    // 根据教师ID查找课程
    List<Course> findByTeacherId(Long teacherId);

    // 根据学期查找课程
    List<Course> findBySemester(String semester);

    // 根据状态查找课程
    List<Course> findByStatus(String status);

    // 根据学分范围查找课程
    List<Course> findByCreditBetween(BigDecimal minCredit, BigDecimal maxCredit);

    // 根据课程名称或编号搜索
    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:keyword% OR c.courseCode LIKE %:keyword%")
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    // 获取热门课程（按选课人数排序）
    @Query("SELECT c FROM Course c WHERE c.status = 'approved' ORDER BY c.currentEnrollment DESC")
    List<Course> findPopularCourses();

    // 获取有剩余名额的课程
    @Query("SELECT c FROM Course c WHERE c.status = 'approved' AND c.currentEnrollment < c.maxCapacity")
    List<Course> findAvailableCourses();

    // 统计教师课程数量
    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacherId = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    // 获取学期列表（去重）
    @Query("SELECT DISTINCT c.semester FROM Course c ORDER BY c.semester DESC")
    List<String> findDistinctSemesters();

    // 获取某个学期的课程统计
    @Query("SELECT COUNT(c), SUM(c.currentEnrollment), SUM(c.maxCapacity) FROM Course c WHERE c.semester = :semester")
    Object[] getSemesterStats(@Param("semester") String semester);
}