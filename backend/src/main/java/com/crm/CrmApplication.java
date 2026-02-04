package com.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;

/**
 * CRM系统主应用类
 */
@SpringBootApplication
@MapperScan("com.crm.mapper")
@ComponentScan(basePackages = "com.crm")
public class CrmApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CrmApplication.class, args);
        
        // 生成测试数据（已注释，避免重复生成）
        /*
        try {
            com.crm.TestDataGenerator testDataGenerator = context.getBean(com.crm.TestDataGenerator.class);
            testDataGenerator.run(args);
        } catch (Exception e) {
            System.out.println("测试数据生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        */
    }
}
