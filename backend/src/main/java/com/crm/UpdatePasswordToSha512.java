package com.crm;

import com.crm.utils.Sha512PasswordEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdatePasswordToSha512 {
    public static void main(String[] args) {
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 连接数据库
            String url = "jdbc:mysql://localhost:3306/crm?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
            String username = "user";
            String password = "612345";
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // 创建SHA512密码编码器
            Sha512PasswordEncoder encoder = new Sha512PasswordEncoder();
            String rawPassword = "123456";
            
            // 查询所有用户
            String querySql = "SELECT id, username FROM sys_user";
            PreparedStatement pstmt = conn.prepareStatement(querySql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                long userId = rs.getLong("id");
                String userUsername = rs.getString("username");
                
                // 生成SHA512+salt密码
                String encodedPassword = encoder.encode(rawPassword);
                System.out.println("更新用户: " + userUsername);
                System.out.println("原始密码: " + rawPassword);
                System.out.println("SHA512+salt密码: " + encodedPassword);
                System.out.println("密码长度: " + encodedPassword.length());
                System.out.println();
                
                // 更新用户密码
                String updateSql = "UPDATE sys_user SET password = ? WHERE id = ?";
                PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
                updatePstmt.setString(1, encodedPassword);
                updatePstmt.setLong(2, userId);
                int rows = updatePstmt.executeUpdate();
                System.out.println("更新成功: " + (rows > 0));
                System.out.println("----------------------------------------");
                
                updatePstmt.close();
            }
            
            // 关闭连接
            rs.close();
            pstmt.close();
            conn.close();
            
            System.out.println("密码更新完成！所有用户密码已更新为SHA512+salt格式。");
            System.out.println("新的登录密码: 123456");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}