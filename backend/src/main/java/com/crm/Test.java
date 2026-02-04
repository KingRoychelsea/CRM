package com.crm;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        // 创建BCrypt密码编码器
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        
        // 测试1: 加密原始密码
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("测试1: 加密原始密码");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后密码: " + encodedPassword);
        
        // 测试2: 验证原始密码与加密后密码是否匹配
        boolean selfMatch = encoder.matches(rawPassword, encodedPassword);
        System.out.println("\n测试2: 验证原始密码与加密后密码是否匹配");
        System.out.println("匹配结果: " + selfMatch);
        
        // 测试3: 验证原始密码与数据库中密码是否匹配
        String dbPassword = "$2a$10$Ux8I7aDlLmvmFyG4OXmjD.r3YItcu6RuuNQiNcfjXjc/GbFvPmwpe";
        boolean dbMatch = encoder.matches(rawPassword, dbPassword);
        System.out.println("\n测试3: 验证原始密码与数据库中密码是否匹配");
        System.out.println("数据库密码: " + dbPassword);
        System.out.println("匹配结果: " + dbMatch);
        
        // 测试4: 验证错误密码与数据库中密码是否匹配
        String wrongPassword = "654321";
        boolean wrongMatch = encoder.matches(wrongPassword, dbPassword);
        System.out.println("\n测试4: 验证错误密码与数据库中密码是否匹配");
        System.out.println("错误密码: " + wrongPassword);
        System.out.println("匹配结果: " + wrongMatch);
        
        // 测试5: 检查数据库密码是否为有效的BCrypt格式
        System.out.println("\n测试5: 检查数据库密码格式");
        System.out.println("数据库密码长度: " + dbPassword.length());
        System.out.println("数据库密码前缀: " + dbPassword.substring(0, 6));
    }
}