package com.course.controller;

import com.course.entity.User;
import com.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取所有用户列表
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", users);
            response.put("total", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取用户列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 根据ID获取用户
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "用户不存在");
                return ResponseEntity.status(404).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 根据用户名获取用户
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "用户不存在");
                return ResponseEntity.status(404).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 创建用户
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            if (userService.findByUsername(user.getUsername()) != null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(error);
            }

            User createdUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户创建成功");
            response.put("data", createdUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "创建用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 更新用户
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            // 查找用户
            User user = userService.getUserById(id);
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "用户不存在");
                return ResponseEntity.status(404).body(error);
            }

            // 更新用户信息
            if (userDetails.getRealName() != null) {
                user.setRealName(userDetails.getRealName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getDepartment() != null) {
                user.setDepartment(userDetails.getDepartment());
            }
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }
            if (userDetails.getStatus() != null) {
                user.setStatus(userDetails.getStatus());
            }

            User updatedUser = userService.updateUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户更新成功");
            response.put("data", updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "更新用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "用户不存在");
                return ResponseEntity.status(404).body(error);
            }

            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "删除用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // 搜索用户
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String department) {
        try {
            // 这里先返回所有用户，实际项目中可以添加搜索逻辑
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", users);
            response.put("total", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "搜索用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}