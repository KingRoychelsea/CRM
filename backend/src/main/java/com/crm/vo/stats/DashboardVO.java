package com.crm.vo.stats;

import lombok.Data;

/**
 * 仪表盘数据VO
 */
@Data
public class DashboardVO {
    private Integer todayCustomerCount; // 今日新增客户
    private Integer thisMonthCustomerCount; // 本月新增客户
    private Integer totalCustomerCount; // 总客户数
    private Integer lossCustomerCount; // 流失客户数
    
    private Double thisMonthSalesAmount; // 本月销售额
    private Double thisQuarterSalesAmount; // 本季度销售额
    private Double thisYearSalesAmount; // 本年销售额
    private Double totalSalesAmount; // 总销售额
    
    private Integer todayOpportunityCount; // 今日新增商机
    private Integer thisMonthOpportunityCount; // 本月新增商机
    private Integer pendingOpportunityCount; // 待跟进商机
    private Double opportunityConversionRate; // 商机转化率
    
    private Integer pendingPaymentCount; // 待回款笔数
    private Double pendingPaymentAmount; // 待回款金额
    private Double thisMonthPaymentAmount; // 本月回款金额
    private Double thisYearPaymentAmount; // 本年回款金额
    
    private TopCustomer[] topCustomers; // top客户
    private TopSales[] topSales; // top销售
    
    @Data
    public static class TopCustomer {
        private String name; // 客户名称
        private Double amount; // 交易金额
    }
    
    @Data
    public static class TopSales {
        private String name; // 销售姓名
        private Double amount; // 销售业绩
    }
}
