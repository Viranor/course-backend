package com.course.service;

import com.course.entity.Course;
import com.course.entity.Selection;
import com.course.entity.User;
import com.course.repository.CourseRepository;
import com.course.repository.SelectionRepository;
import com.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SelectionService {

    @Autowired
    private SelectionRepository selectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    // 学生选课
    public Selection selectCourse(Long studentId, Long courseId, String semester) {
        // 验证学生存在且是学生角色
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        if (!"student".equals(student.getRole())) {
            throw new RuntimeException("只有学生可以选课");
        }

        // 验证课程存在且开放
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        if (!"approved".equals(course.getStatus())) {
            throw new RuntimeException("该课程暂不开放选课");
        }

        // 检查是否已选该课程
        Optional<Selection> existing = selectionRepository.findByStudentAndCourseAndSemester(
                studentId, courseId, semester
        );

        if (existing.isPresent()) {
            Selection selection = existing.get();
            if ("selected".equals(selection.getStatus())) {
                throw new RuntimeException("您已选择该课程");
            } else {
                // 如果是已退选状态，重新选课
                selection.setStatus("selected");
                selection.setSelectTime(LocalDateTime.now());
                selection.setUpdateTime(LocalDateTime.now());
                return selectionRepository.save(selection);
            }
        }

        // 检查课程容量
        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new RuntimeException("课程人数已满");
        }

        // 创建选课记录
        Selection selection = new Selection();
        selection.setStudent(student);
        selection.setCourse(course);
        selection.setSemester(semester);
        selection.setStatus("selected");
        selection.setSelectTime(LocalDateTime.now());
        selection.setCreateTime(LocalDateTime.now());
        selection.setUpdateTime(LocalDateTime.now());

        // 保存选课记录
        Selection savedSelection = selectionRepository.save(selection);

        // 更新课程选课人数
        courseService.incrementEnrollment(courseId);

        return savedSelection;
    }

    // 学生退课
    public Selection dropCourse(Long selectionId) {
        Selection selection = selectionRepository.findById(selectionId)
                .orElseThrow(() -> new RuntimeException("选课记录不存在"));

        if (!"selected".equals(selection.getStatus())) {
            throw new RuntimeException("该课程尚未选中");
        }

        // 更新选课状态
        selection.setStatus("dropped");
        selection.setUpdateTime(LocalDateTime.now());
        Selection updatedSelection = selectionRepository.save(selection);

        // 更新课程选课人数
        courseService.decrementEnrollment(selection.getCourse().getId());

        return updatedSelection;
    }

    // 获取学生的选课记录
    public List<Selection> getStudentSelections(Long studentId) {
        return selectionRepository.findByStudentId(studentId);
    }

    // 获取学生的当前学期选课
    public List<Selection> getStudentCurrentSelections(Long studentId, String semester) {
        return selectionRepository.findByStudentAndSemester(studentId, semester);
    }

    // 获取课程的所有选课记录
    public List<Selection> getCourseSelections(Long courseId) {
        return selectionRepository.findByCourseId(courseId);
    }

    // 获取学生的课表
    public List<Selection> getStudentTimetable(Long studentId, String semester) {
        return selectionRepository.findStudentTimetable(studentId, semester);
    }

    // 获取学生的已选学分
    public Integer getStudentCredits(Long studentId, String semester) {
        Integer credits = selectionRepository.sumCreditsByStudentAndSemester(studentId, semester);
        return credits != null ? credits : 0;
    }

    // 检查是否已选某门课程
    public boolean isCourseSelected(Long studentId, Long courseId, String semester) {
        Optional<Selection> selection = selectionRepository.findByStudentAndCourseAndSemester(
                studentId, courseId, semester
        );
        return selection.isPresent() && "selected".equals(selection.get().getStatus());
    }

    // 获取选课统计
    public SelectionStats getSelectionStats() {
        SelectionStats stats = new SelectionStats();

        // 这里可以添加更多统计逻辑
        // 例如：总选课数、当前学期选课数等

        return stats;
    }

    // 选课统计内部类
    public static class SelectionStats {
        // 可以根据需要添加统计字段
    }
}