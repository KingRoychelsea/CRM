package com.crm.controller.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.dto.sales.PaymentDTO;
import com.crm.service.sales.PaymentService;
import com.crm.vo.sales.PaymentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 回款信息控制器
 */
@Api(tags = "回款管理")
@RestController
@RequestMapping("/api/sales/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 回款分页查询
     */
    @ApiOperation("回款分页查询")
    @GetMapping("/list")
    public Result<Page<PaymentVO>> list(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String paymentName,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long contractId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer paymentMethod,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Page<PaymentVO> page = paymentService.list(pageNum, pageSize, paymentName, customerId, orderId, contractId, status, paymentMethod, ownerId, deptId, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取回款详情
     */
    @ApiOperation("获取回款详情")
    @GetMapping("/get/{id}")
    public Result<PaymentVO> getById(@PathVariable Long id) {
        PaymentVO paymentVO = paymentService.getById(id);
        return Result.success(paymentVO);
    }

    /**
     * 新增回款
     */
    @ApiOperation("新增回款")
    @PostMapping("/add")
    public Result<?> add(@RequestBody PaymentDTO paymentDTO) {
        paymentService.add(paymentDTO);
        return Result.success();
    }

    /**
     * 修改回款
     */
    @ApiOperation("修改回款")
    @PutMapping("/update")
    public Result<?> update(@RequestBody PaymentDTO paymentDTO) {
        paymentService.update(paymentDTO);
        return Result.success();
    }

    /**
     * 删除回款
     */
    @ApiOperation("删除回款")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除回款
     */
    @ApiOperation("批量删除回款")
    @DeleteMapping("/batchDelete")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        paymentService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 更新回款状态
     */
    @ApiOperation("更新回款状态")
    @PutMapping("/updateStatus")
    public Result<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        paymentService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 导出回款
     */
    @ApiOperation("导出回款")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(required = false) String paymentName,
                            @RequestParam(required = false) Long customerId,
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) Long ownerId,
                            @RequestParam(required = false) Long deptId,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        paymentService.exportExcel(response, paymentName, customerId, status, ownerId, deptId, startTime, endTime);
    }

    /**
     * 获取未回款提醒
     */
    @ApiOperation("获取未回款提醒")
    @GetMapping("/remind")
    public Result<List<PaymentVO>> getPaymentRemind() {
        List<PaymentVO> paymentVOs = paymentService.getPaymentRemind();
        return Result.success(paymentVOs);
    }

    /**
     * 获取回款统计数据
     */
    @ApiOperation("获取回款统计数据")
    @GetMapping("/stats")
    public Result<List<PaymentVO>> getPaymentStats() {
        List<PaymentVO> paymentVOs = paymentService.getPaymentStats();
        return Result.success(paymentVOs);
    }
}
