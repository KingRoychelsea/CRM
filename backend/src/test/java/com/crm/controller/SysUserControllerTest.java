package com.crm.controller;

import com.crm.dto.sys.SysUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SysUserControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/api/sys/user";

    @Test
    public void testLogin() throws Exception {
        // 测试登录功能
        SysUserDTO loginDTO = new SysUserDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        // 测试获取当前用户信息
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/current")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    public void testListUsers() throws Exception {
        // 测试获取用户列表
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
    }

    @Test
    public void testAddUser() throws Exception {
        // 测试添加用户
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("123456");
        userDTO.setRealName("测试用户");
        userDTO.setEmail("test@example.com");
        userDTO.setPhone("13800138000");
        userDTO.setDeptId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/add")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // 测试更新用户
        SysUserDTO userDTO = new SysUserDTO();
        userDTO.setId(1L);
        userDTO.setRealName("更新后的用户名");
        userDTO.setEmail("updated@example.com");

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/update")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // 测试删除用户
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/delete/2")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testResetPassword() throws Exception {
        // 测试重置密码
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/resetPassword/1")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }
}
