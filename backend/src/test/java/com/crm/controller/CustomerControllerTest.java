package com.crm.controller;

import com.crm.dto.customer.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/api/customer";

    @Test
    public void testListCustomers() throws Exception {
        // 测试获取客户列表
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
    }

    @Test
    public void testGetCustomerById() throws Exception {
        // 测试根据ID获取客户信息
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    public void testAddCustomer() throws Exception {
        // 测试添加客户
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("测试客户");
        customerDTO.setContactName("张三");
        customerDTO.setPhone("13800138000");
        customerDTO.setEmail("test@example.com");
        customerDTO.setAddress("北京市朝阳区");
        customerDTO.setIndustry("IT");
        customerDTO.setLevel("A");

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/add")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        // 测试更新客户
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("更新后的客户名称");
        customerDTO.setContactName("李四");
        customerDTO.setPhone("13900139000");

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/update")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        // 测试删除客户
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/delete/2")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testBatchDeleteCustomers() throws Exception {
        // 测试批量删除客户
        Long[] ids = {3L, 4L};

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/batchDelete")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(ids)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }
}
