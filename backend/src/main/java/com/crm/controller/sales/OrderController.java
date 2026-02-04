package com.crm.controller.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.dto.sales.OrderDTO;
import com.crm.service.sales.OrderService;
import com.crm.vo.sales.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单信息控制器
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/sales/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     */
    @ApiOperation("订单分页查询")
    @GetMapping("/list")
    public Result<Page<OrderVO>> list(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String orderName,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long contractId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Page<OrderVO> page = orderService.list(pageNum, pageSize, orderName, customerId, contractId, status, ownerId, deptId, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取订单详情
     */
    @ApiOperation("获取订单详情")
    @GetMapping("/get/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 新增订单
     */
    @ApiOperation("新增订单")
    @PostMapping("/add")
    public Result<?> add(@RequestBody OrderDTO orderDTO) {
        orderService.add(orderDTO);
        return Result.success();
    }

    /**
     * 修改订单
     */
    @ApiOperation("修改订单")
    @PutMapping("/update")
    public Result<?> update(@RequestBody OrderDTO orderDTO) {
        orderService.update(orderDTO);
        return Result.success();
    }

    /**
     * 删除订单
     */
    @ApiOperation("删除订单")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除订单
     */
    @ApiOperation("批量删除订单")
    @DeleteMapping("/batchDelete")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        orderService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 更新订单状态
     */
    @ApiOperation("更新订单状态")
    @PutMapping("/updateStatus")
    public Result<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        orderService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 导出订单
     */
    @ApiOperation("导出订单")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(required = false) String orderName,
                            @RequestParam(required = false) Long customerId,
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) Long ownerId,
                            @RequestParam(required = false) Long deptId,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        orderService.exportExcel(response, orderName, customerId, status, ownerId, deptId, startTime, endTime);
    }

    /**
     * 获取订单下拉列表
     */
    @ApiOperation("获取订单下拉列表")
    @GetMapping("/select")
    public Result<List<OrderVO>> getOrderSelect() {
        List<OrderVO> orderVOs = orderService.getOrderSelect();
        return Result.success(orderVOs);
    }

    /**
     * 获取订单统计数据
     */
    @ApiOperation("获取订单统计数据")
    @GetMapping("/stats")
    public Result<List<OrderVO>> getOrderStats() {
        List<OrderVO> orderVOs = orderService.getOrderStats();
        return Result.success(orderVOs);
    }
}
