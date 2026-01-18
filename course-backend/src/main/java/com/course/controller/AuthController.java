package com.course.controller;

import com.course.dto.AuthRequest;
import com.course.dto.AuthResponse;
import com.course.dto.RegisterRequest;
import com.course.entity.User;
import com.course.service.UserService;
import com.course.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // 检查用户名是否已存在
            if (userService.findByUsername(request.getUsername()) != null) {
                return ResponseEntity.badRequest().body(createErrorResponse("用户名已存在"));
            }

            // 创建新用户
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRealName(request.getName());
            newUser.setEmail(request.getEmail());
            newUser.setRole(request.getRole());
            newUser.setStatus("active");

            // 保存用户
            User savedUser = userService.createUser(newUser);

            // 生成JWT令牌
            String token = jwtUtil.generateToken(
                    savedUser.getUsername(),
                    savedUser.getId(),
                    savedUser.getRole()
            );

            // 返回响应
            AuthResponse response = new AuthResponse(
                    token,
                    savedUser.getUsername(),
                    savedUser.getId(),
                    savedUser.getRole()
            );
            response.setMessage("注册成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("注册失败: " + e.getMessage()));
        }
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            // 查找用户
            User user = userService.findByUsername(request.getUsername());
            if (user == null) {
                return ResponseEntity.status(401).body(createErrorResponse("用户名或密码错误"));
            }

            // 验证密码
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(createErrorResponse("用户名或密码错误"));
            }

            // 生成JWT令牌
            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getId(),
                    user.getRole()
            );

            // 返回响应
            AuthResponse response = new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getId(),
                    user.getRole()
            );
            response.setMessage("登录成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("登录失败: " + e.getMessage()));
        }
    }

    // 获取当前用户信息
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(createErrorResponse("用户未登录"));
            }

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(404).body(createErrorResponse("用户不存在"));
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("realName", user.getRealName());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            userInfo.put("status", user.getStatus());
            userInfo.put("createTime", user.getCreateTime());

            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("获取用户信息失败: " + e.getMessage()));
        }
    }

    // 验证Token
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(createErrorResponse("无效的Token格式"));
            }

            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", jwtUtil.getUsernameFromToken(token));
                response.put("userId", jwtUtil.getUserIdFromToken(token));
                response.put("role", jwtUtil.getRoleFromToken(token));
                response.put("message", "Token有效");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(createErrorResponse("Token无效或已过期"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(createErrorResponse("Token验证失败: " + e.getMessage()));
        }
    }

    // 创建错误响应
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}