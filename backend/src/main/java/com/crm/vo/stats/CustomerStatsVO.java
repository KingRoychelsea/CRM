package com.crm.vo.stats;

import lombok.Data;

/**
 * 客户统计VO
 */
@Data
public class CustomerStatsVO {
    private String name; // 统计维度名称（如月份、分类等）
    private Integer count; // 客户数量
    private Integer potentialCount; // 潜在客户数量
    private Integer intentCount; // 意向客户数量
    private Integer cooperationCount; // 合作客户数量
    private Integer lossCount; // 流失客户数量
}
