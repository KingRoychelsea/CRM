package com.crm.controller;

import com.crm.dto.sales.BusinessOpportunityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BusinessOpportunityControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/api/sales/opportunity";

    @Test
    public void testListOpportunities() throws Exception {
        // 测试获取商机列表
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
    }

    @Test
    public void testGetOpportunityById() throws Exception {
        // 测试根据ID获取商机信息
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    public void testAddOpportunity() throws Exception {
        // 测试添加商机
        BusinessOpportunityDTO opportunityDTO = new BusinessOpportunityDTO();
        opportunityDTO.setCustomerId(1L);
        opportunityDTO.setName("测试商机");
        opportunityDTO.setAmount(100000.00);
        opportunityDTO.setStage("初步接触");
        opportunityDTO.setProbability(30);
        opportunityDTO.setExpectedCloseDate("2026-12-31");
        opportunityDTO.setOwnerId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/add")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(opportunityDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testUpdateOpportunity() throws Exception {
        // 测试更新商机
        BusinessOpportunityDTO opportunityDTO = new BusinessOpportunityDTO();
        opportunityDTO.setId(1L);
        opportunityDTO.setName("更新后的商机名称");
        opportunityDTO.setAmount(150000.00);
        opportunityDTO.setStage("需求分析");
        opportunityDTO.setProbability(50);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/update")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(opportunityDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testDeleteOpportunity() throws Exception {
        // 测试删除商机
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/delete/2")
                .header("Authorization", getToken()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testUpdateStage() throws Exception {
        // 测试更新商机阶段
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/updateStage/1")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"stage\": \"商务谈判\", \"probability\": 80}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    @Test
    public void testCloseOpportunity() throws Exception {
        // 测试关闭商机
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/close/1")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"成功\", \"actualAmount\": 120000.00}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }
}
