-- 确保user表存在（使用反引号转义）
CREATE TABLE IF NOT EXISTS `user` (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 插入测试数据
MERGE INTO `user` (username, password, real_name, role, email) VALUES
('admin', '123456', '系统管理员', 'admin', 'admin@example.com'),
('teacher1', '123456', '张老师', 'teacher', 'teacher@example.com'),
('student1', '123456', '王同学', 'student', 'student@example.com');