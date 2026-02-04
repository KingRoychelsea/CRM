package com.crm.controller;

import com.crm.config.ApiPathConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API路径验证测试
 * 验证所有API端点的路径配置是否正确
 */
@SpringBootTest
@AutoConfigureTestMvc
public class ApiPathValidationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 测试用户管理API路径
     */
    @Test
    public void testUserApiPaths() throws Exception {
        // 测试用户列表路径
        mockMvc.perform(get(ApiPathConfig.USER_PATH + "/list"))
                .andExpect(status().isUnauthorized()); // 401表示路径存在但无权限
        
        // 测试用户登录路径
        mockMvc.perform(post(ApiPathConfig.USER_PATH + "/login")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest()); // 400表示路径存在但参数错误
    }

    /**
     * 测试角色管理API路径
     */
    @Test
    public void testRoleApiPaths() throws Exception {
        mockMvc.perform(get(ApiPathConfig.ROLE_PATH + "/list"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试部门管理API路径
     */
    @Test
    public void testDeptApiPaths() throws Exception {
        mockMvc.perform(get(ApiPathConfig.DEPT_PATH + "/tree"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试菜单管理API路径
     */
    @Test
    public void testMenuApiPaths() throws Exception {
        mockMvc.perform(get(ApiPathConfig.MENU_PATH + "/tree"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试字典管理API路径
     */
    @Test
    public void testDictApiPaths() throws Exception {
        mockMvc.perform(get(ApiPathConfig.DICT_PATH + "/list"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试认证管理API路径
     */
    @Test
    public void testAuthApiPaths() throws Exception {
        mockMvc.perform(post(ApiPathConfig.AUTH_PATH + "/login")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * 测试健康检查API路径
     */
    @Test
    public void testHealthApiPaths() throws Exception {
        mockMvc.perform(get(ApiPathConfig.HEALTH_PATH + "/status"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get(ApiPathConfig.HEALTH_PATH + "/endpoints"))
                .andExpect(status().isOk());
    }

    /**
     * 测试API路径配置正确性
     */
    @Test
    public void testApiPathConfiguration() {
        // 验证路径不为空
        assert ApiPathConfig.USER_PATH != null && !ApiPathConfig.USER_PATH.isEmpty();
        assert ApiPathConfig.ROLE_PATH != null && !ApiPathConfig.ROLE_PATH.isEmpty();
        assert ApiPathConfig.DEPT_PATH != null && !ApiPathConfig.DEPT_PATH.isEmpty();
        assert ApiPathConfig.MENU_PATH != null && !ApiPathConfig.MENU_PATH.isEmpty();
        assert ApiPathConfig.DICT_PATH != null && !ApiPathConfig.DICT_PATH.isEmpty();
        assert ApiPathConfig.AUTH_PATH != null && !ApiPathConfig.AUTH_PATH.isEmpty();
        assert ApiPathConfig.HEALTH_PATH != null && !ApiPathConfig.HEALTH_PATH.isEmpty();
        
        // 验证路径格式正确
        assert ApiPathConfig.USER_PATH.startsWith("/api");
        assert ApiPathConfig.ROLE_PATH.startsWith("/api");
        assert ApiPathConfig.DEPT_PATH.startsWith("/api");
        assert ApiPathConfig.MENU_PATH.startsWith("/api");
        assert ApiPathConfig.DICT_PATH.startsWith("/api");
        assert ApiPathConfig.AUTH_PATH.startsWith("/api");
        assert ApiPathConfig.HEALTH_PATH.startsWith("/api");
    }

    /**
     * 测试路径构建工具方法
     */
    @Test
    public void testPathBuildingMethods() {
        // 测试获取完整路径
        String fullPath = ApiPathConfig.getFullPath(ApiPathConfig.USER_PATH, "list");
        assert fullPath.equals(ApiPathConfig.USER_PATH + "/list");
        
        // 测试获取带ID的路径
        String pathWithId = ApiPathConfig.getPathWithId(ApiPathConfig.USER_PATH, 1L);
        assert pathWithId.equals(ApiPathConfig.USER_PATH + "/1");
        
        // 测试获取带ID和子路径的路径
        String pathWithIdAndSubPath = ApiPathConfig.getPathWithIdAndSubPath(ApiPathConfig.USER_PATH, 1L, "roles");
        assert pathWithIdAndSubPath.equals(ApiPathConfig.USER_PATH + "/1/roles");
    }
}