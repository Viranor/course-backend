package com.course.repository;

import com.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 添加一些查询方法
    List<User> findByRole(String role);
    List<User> findByDepartment(String department);
    List<User> findByStatus(String status);

    // 搜索用户
    @Query("SELECT u FROM User u WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:department IS NULL OR u.department LIKE %:department%)")
    List<User> searchUsers(@Param("username") String username,
                           @Param("role") String role,
                           @Param("department") String department);
}