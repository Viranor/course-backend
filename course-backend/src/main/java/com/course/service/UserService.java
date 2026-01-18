package com.course.service;

import com.course.entity.User;
import com.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User createUser(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            // 只更新允许的字段
            if (user.getRealName() != null) {
                userToUpdate.setRealName(user.getRealName());
            }
            if (user.getEmail() != null) {
                userToUpdate.setEmail(user.getEmail());
            }
            if (user.getDepartment() != null) {
                userToUpdate.setDepartment(user.getDepartment());
            }
            if (user.getRole() != null) {
                userToUpdate.setRole(user.getRole());
            }
            if (user.getStatus() != null) {
                userToUpdate.setStatus(user.getStatus());
            }
            userToUpdate.setUpdateTime(LocalDateTime.now());
            return userRepository.save(userToUpdate);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}