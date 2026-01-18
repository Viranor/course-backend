package com.course.repository;

import com.course.entity.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long> {

    // 根据学生ID查询选课记录
    List<Selection> findByStudentId(Long studentId);

    // 根据课程ID查询选课记录
    List<Selection> findByCourseId(Long courseId);

    // 根据学期查询选课记录
    List<Selection> findBySemester(String semester);

    // 根据状态查询选课记录
    List<Selection> findByStatus(String status);

    // 查询学生的选课记录（按学期分组）
    @Query("SELECT s FROM Selection s WHERE s.student.id = :studentId AND s.semester = :semester")
    List<Selection> findByStudentAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    // 查询学生是否已选某门课程
    @Query("SELECT s FROM Selection s WHERE s.student.id = :studentId AND s.course.id = :courseId AND s.semester = :semester")
    Optional<Selection> findByStudentAndCourseAndSemester(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("semester") String semester
    );

    // 查询课程的选课人数
    @Query("SELECT COUNT(s) FROM Selection s WHERE s.course.id = :courseId AND s.semester = :semester AND s.status = 'selected'")
    long countByCourseAndSemester(@Param("courseId") Long courseId, @Param("semester") String semester);

    // 查询学生的已选学分
    @Query("SELECT SUM(c.credit) FROM Selection s JOIN s.course c WHERE s.student.id = :studentId AND s.semester = :semester AND s.status = 'selected'")
    Integer sumCreditsByStudentAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    // 查询学生的课程表（按时间排序）
    @Query("SELECT s FROM Selection s WHERE s.student.id = :studentId AND s.semester = :semester AND s.status = 'selected' ORDER BY s.course.timeSlot")
    List<Selection> findStudentTimetable(@Param("studentId") Long studentId, @Param("semester") String semester);
}