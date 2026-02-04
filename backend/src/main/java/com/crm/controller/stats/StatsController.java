package com.crm.controller.stats;

import com.crm.common.Result;
import com.crm.service.stats.StatsService;
import com.crm.vo.stats.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据统计控制器
 */
@Api(tags = "数据统计")
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * 获取客户统计数据
     */
    @ApiOperation("获取客户统计数据")
    @GetMapping("/customer")
    public Result<List<CustomerStatsVO>> getCustomerStats(
            @RequestParam(required = false, defaultValue = "time") String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<CustomerStatsVO> stats = statsService.getCustomerStats(type, startTime, endTime);
        return Result.success(stats);
    }

    /**
     * 获取销售业绩统计数据
     */
    @ApiOperation("获取销售业绩统计数据")
    @GetMapping("/sales")
    public Result<List<SalesStatsVO>> getSalesStats(
            @RequestParam(required = false, defaultValue = "time") String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<SalesStatsVO> stats = statsService.getSalesStats(type, startTime, endTime);
        return Result.success(stats);
    }

    /**
     * 获取商机统计数据
     */
    @ApiOperation("获取商机统计数据")
    @GetMapping("/opportunity")
    public Result<List<OpportunityStatsVO>> getOpportunityStats(
            @RequestParam(required = false, defaultValue = "stage") String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<OpportunityStatsVO> stats = statsService.getOpportunityStats(type, startTime, endTime);
        return Result.success(stats);
    }

    /**
     * 获取回款统计数据
     */
    @ApiOperation("获取回款统计数据")
    @GetMapping("/payment")
    public Result<List<PaymentStatsVO>> getPaymentStats(
            @RequestParam(required = false, defaultValue = "time") String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<PaymentStatsVO> stats = statsService.getPaymentStats(type, startTime, endTime);
        return Result.success(stats);
    }

    /**
     * 获取仪表盘数据
     */
    @ApiOperation("获取仪表盘数据")
    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboardData() {
        DashboardVO dashboard = statsService.getDashboardData();
        return Result.success(dashboard);
    }

    /**
     * 导出客户统计报表
     */
    @ApiOperation("导出客户统计报表")
    @GetMapping("/export/customer")
    public void exportCustomerStats(HttpServletResponse response,
                                    @RequestParam(required = false, defaultValue = "time") String type,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime) {
        statsService.exportCustomerStats(response, type, startTime, endTime);
    }

    /**
     * 导出销售业绩报表
     */
    @ApiOperation("导出销售业绩报表")
    @GetMapping("/export/sales")
    public void exportSalesStats(HttpServletResponse response,
                                 @RequestParam(required = false, defaultValue = "time") String type,
                                 @RequestParam(required = false) String startTime,
                                 @RequestParam(required = false) String endTime) {
        statsService.exportSalesStats(response, type, startTime, endTime);
    }

    /**
     * 导出商机统计报表
     */
    @ApiOperation("导出商机统计报表")
    @GetMapping("/export/opportunity")
    public void exportOpportunityStats(HttpServletResponse response,
                                       @RequestParam(required = false, defaultValue = "stage") String type,
                                       @RequestParam(required = false) String startTime,
                                       @RequestParam(required = false) String endTime) {
        statsService.exportOpportunityStats(response, type, startTime, endTime);
    }

    /**
     * 导出回款统计报表
     */
    @ApiOperation("导出回款统计报表")
    @GetMapping("/export/payment")
    public void exportPaymentStats(HttpServletResponse response,
                                   @RequestParam(required = false, defaultValue = "time") String type,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endTime) {
        statsService.exportPaymentStats(response, type, startTime, endTime);
    }
}
