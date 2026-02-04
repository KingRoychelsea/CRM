package com.crm.vo.stats;

import lombok.Data;

/**
 * 销售业绩统计VO
 */
@Data
public class SalesStatsVO {
    private String name; // 统计维度名称（如月份、员工、部门等）
    private Double contractAmount; // 合同金额
    private Double orderAmount; // 订单金额
    private Double paymentAmount; // 回款金额
    private Integer contractCount; // 合同数量
    private Integer orderCount; // 订单数量
    private Integer paymentCount; // 回款数量
}
