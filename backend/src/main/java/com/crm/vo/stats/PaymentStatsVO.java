package com.crm.vo.stats;

import lombok.Data;

/**
 * 回款统计VO
 */
@Data
public class PaymentStatsVO {
    private String name; // 统计维度名称（如月份、付款方式等）
    private Double amount; // 回款金额
    private Integer count; // 回款笔数
    private Double plannedAmount; // 计划回款金额
    private Double actualAmount; // 实际回款金额
    private Double completionRate; // 完成率
}
