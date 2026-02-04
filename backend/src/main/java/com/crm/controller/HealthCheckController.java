package com.crm.controller;

import com.crm.common.Result;
import com.crm.config.ApiPathConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API健康检查控制器
 * 提供API端点的健康状态检查和监控
 */
@Api(tags = "API健康检查")
@RestController
@RequestMapping(ApiPathConfig.HEALTH_PATH)
public class HealthCheckController {

    @Autowired
    private HealthCheckService healthCheckService;

    /**
     * 获取API健康状态
     */
    @ApiOperation("获取API健康状态")
    @GetMapping("/status")
    public Result<Map<String, Object>> getHealthStatus() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        // 检查数据库连接
        boolean dbHealthy = healthCheckService.checkDatabaseHealth();
        
        // 检查各个模块的API端点
        Map<String, Boolean> apiEndpoints = new HashMap<>();
        apiEndpoints.put("auth", healthCheckService.checkAuthEndpoint());
        apiEndpoints.put("user", healthCheckService.checkUserEndpoint());
        apiEndpoints.put("role", healthCheckService.checkRoleEndpoint());
        apiEndpoints.put("dept", healthCheckService.checkDeptEndpoint());
        apiEndpoints.put("menu", healthCheckService.checkMenuEndpoint());
        apiEndpoints.put("dict", healthCheckService.checkDictEndpoint());
        apiEndpoints.put("log", healthCheckService.checkLogEndpoint());
        apiEndpoints.put("customer", healthCheckService.checkCustomerEndpoint());
        apiEndpoints.put("sales", healthCheckService.checkSalesEndpoint());
        apiEndpoints.put("stats", healthCheckService.checkStatsEndpoint());
        
        healthStatus.put("status", dbHealthy ? "UP" : "DOWN");
        healthStatus.put("database", dbHealthy ? "HEALTHY" : "UNHEALTHY");
        healthStatus.put("endpoints", apiEndpoints);
        healthStatus.put("timestamp", System.currentTimeMillis());
        
        return Result.success(healthStatus);
    }

    /**
     * 获取API端点列表
     */
    @ApiOperation("获取API端点列表")
    @GetMapping("/endpoints")
    public Result<Map<String, String>> getApiEndpoints() {
        Map<String, String> endpoints = new HashMap<>();
        
        endpoints.put("auth", ApiPathConfig.AUTH_PATH);
        endpoints.put("user", ApiPathConfig.USER_PATH);
        endpoints.put("role", ApiPathConfig.ROLE_PATH);
        endpoints.put("dept", ApiPathConfig.DEPT_PATH);
        endpoints.put("menu", ApiPathConfig.MENU_PATH);
        endpoints.put("dict", ApiPathConfig.DICT_PATH);
        endpoints.put("log", ApiPathConfig.LOG_PATH);
        endpoints.put("customer", ApiPathConfig.CUSTOMER_PATH);
        endpoints.put("sales", ApiPathConfig.SALES_PATH);
        endpoints.put("stats", ApiPathConfig.STATS_PATH);
        
        return Result.success(endpoints);
    }
}