package com.crm.vo.stats;

import lombok.Data;

/**
 * 商机统计VO
 */
@Data
public class OpportunityStatsVO {
    private String name; // 统计维度名称（如月份、阶段等）
    private Integer count; // 商机数量
    private Double amount; // 商机金额
    private Integer contactCount; // 初步接洽数量
    private Integer demandCount; // 需求确认数量
    private Integer proposalCount; // 方案报价数量
    private Integer contractCount; // 合同签订数量
    private Double conversionRate; // 转化率
}
