package com.course.controller;

import com.course.common.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class DatabaseController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 测试数据库连接
    @GetMapping("/test")
    public Result<Object> testConnection() {
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT 1 as test");
            return Result.success("H2数据库连接成功", result);
        } catch (Exception e) {
            return Result.error("数据库连接失败: " + e.getMessage());
        }
    }

    // 查看所有表（H2兼容语法）
    @GetMapping("/tables")
    public Result<Object> showTables() {
        try {
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                    "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'"
            );
            return Result.success("数据库表列表", tables);
        } catch (Exception e) {
            return Result.error("获取表失败: " + e.getMessage());
        }
    }

    // 查看数据库版本（H2兼容语法）
    @GetMapping("/version")
    public Result<Object> getVersion() {
        try {
            String version = jdbcTemplate.queryForObject(
                    "SELECT H2VERSION()", String.class
            );
            return Result.success("H2数据库版本", Map.of("version", version));
        } catch (Exception e) {
            return Result.error("获取版本失败: " + e.getMessage());
        }
    }

    // 查看表结构（H2兼容语法）
    @GetMapping("/table/{tableName}")
    public Result<Object> getTableInfo(@PathVariable String tableName) {
        if (!isValidTableName(tableName)) {
            return Result.error("无效的表名");
        }

        try {
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                    "SELECT COLUMN_NAME, TYPE_NAME, IS_NULLABLE, COLUMN_DEFAULT " +
                            "FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = 'PUBLIC'",
                    tableName.toUpperCase()
            );
            return Result.success("表结构: " + tableName, columns);
        } catch (Exception e) {
            return Result.error("获取表结构失败: " + e.getMessage());
        }
    }

    // 验证表名合法性
    private boolean isValidTableName(String tableName) {
        return tableName != null && tableName.matches("^[a-zA-Z0-9_]+$");
    }
}