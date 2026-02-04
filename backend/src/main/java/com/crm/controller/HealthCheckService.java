package com.crm.controller;

import com.crm.config.ApiPathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 健康检查服务
 * 提供各个模块的健康状态检查
 */
@Service
public class HealthCheckService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 检查数据库连接
     */
    public boolean checkDatabaseHealth() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查认证端点
     */
    public boolean checkAuthEndpoint() {
        try {
            // 检查认证端点是否可访问（忽略认证错误）
            String url = "http://localhost:8080" + ApiPathConfig.AUTH_PATH + "/login";
            restTemplate.postForObject(url, "{}", String.class);
            return true;
        } catch (Exception e) {
            // 如果是认证错误，说明端点存在
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查用户管理端点
     */
    public boolean checkUserEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.USER_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            // 如果是认证错误，说明端点存在
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查角色管理端点
     */
    public boolean checkRoleEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.ROLE_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查部门管理端点
     */
    public boolean checkDeptEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.DEPT_PATH + "/tree";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查菜单管理端点
     */
    public boolean checkMenuEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.MENU_PATH + "/tree";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查字典管理端点
     */
    public boolean checkDictEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.DICT_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查日志管理端点
     */
    public boolean checkLogEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.LOG_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查客户管理端点
     */
    public boolean checkCustomerEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.CUSTOMER_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查销售管理端点
     */
    public boolean checkSalesEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.SALES_PATH + "/list";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }

    /**
     * 检查统计管理端点
     */
    public boolean checkStatsEndpoint() {
        try {
            String url = "http://localhost:8080" + ApiPathConfig.STATS_PATH + "/dashboard";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return e.getMessage().contains("401") || e.getMessage().contains("Unauthorized");
        }
    }
}