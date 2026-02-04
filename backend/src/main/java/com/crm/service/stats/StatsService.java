package com.crm.service.stats;

import com.crm.vo.stats.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据统计服务接口
 */
public interface StatsService {

    /**
     * 获取客户统计数据
     */
    List<CustomerStatsVO> getCustomerStats(String type, String startTime, String endTime);

    /**
     * 获取销售业绩统计数据
     */
    List<SalesStatsVO> getSalesStats(String type, String startTime, String endTime);

    /**
     * 获取商机统计数据
     */
    List<OpportunityStatsVO> getOpportunityStats(String type, String startTime, String endTime);

    /**
     * 获取回款统计数据
     */
    List<PaymentStatsVO> getPaymentStats(String type, String startTime, String endTime);

    /**
     * 获取仪表盘数据
     */
    DashboardVO getDashboardData();

    /**
     * 导出客户统计报表
     */
    void exportCustomerStats(HttpServletResponse response, String type, String startTime, String endTime);

    /**
     * 导出销售业绩报表
     */
    void exportSalesStats(HttpServletResponse response, String type, String startTime, String endTime);

    /**
     * 导出商机统计报表
     */
    void exportOpportunityStats(HttpServletResponse response, String type, String startTime, String endTime);

    /**
     * 导出回款统计报表
     */
    void exportPaymentStats(HttpServletResponse response, String type, String startTime, String endTime);
}
