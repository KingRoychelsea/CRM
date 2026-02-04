package com.crm;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdatePassword {
    public static void main(String[] args) {
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 连接数据库
            String url = "jdbc:mysql://localhost:3306/crm?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
            String username = "user";
            String password = "612345";
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // 创建BCrypt密码编码器
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "123456";
            String encodedPassword = encoder.encode(rawPassword);
            
            System.out.println("原始密码: " + rawPassword);
            System.out.println("加密后密码: " + encodedPassword);
            System.out.println("密码长度: " + encodedPassword.length());
            
            // 更新admin用户密码
            String updateSql = "UPDATE sys_user SET password = ? WHERE username = 'admin'";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, encodedPassword);
            int adminRows = pstmt.executeUpdate();
            System.out.println("\n更新admin用户密码成功: " + (adminRows > 0));
            
            // 更新user用户密码
            updateSql = "UPDATE sys_user SET password = ? WHERE username = 'user'";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, encodedPassword);
            int userRows = pstmt.executeUpdate();
            System.out.println("更新user用户密码成功: " + (userRows > 0));
            
            // 验证密码是否匹配
            boolean matches = encoder.matches(rawPassword, encodedPassword);
            System.out.println("\n密码验证结果: " + matches);
            
            // 关闭连接
            pstmt.close();
            conn.close();
            
            System.out.println("\n密码更新完成！");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}