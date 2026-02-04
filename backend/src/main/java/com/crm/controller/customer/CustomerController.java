package com.crm.controller.customer;

import com.crm.common.Result;
import com.crm.dto.customer.CustomerDTO;
import com.crm.service.customer.CustomerService;
import com.crm.vo.customer.CustomerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 客户信息控制器
 */
@Api(tags = "客户管理")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户分页查询
     */
    @ApiOperation("客户分页查询")
    @PreAuthorize("hasAuthority('customer:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("客户名称") @RequestParam(required = false) String customerName,
            @ApiParam("联系电话") @RequestParam(required = false) String phone,
            @ApiParam("客户类型") @RequestParam(required = false) Integer customerType,
            @ApiParam("负责人ID") @RequestParam(required = false) Long ownerId,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId
    ) {
        return Result.success(customerService.list(pageNum, pageSize, customerName, phone, customerType, ownerId, deptId));
    }

    /**
     * 获取客户详情
     */
    @ApiOperation("获取客户详情")
    @PreAuthorize("hasAuthority('customer:query')")
    @GetMapping("/{id}")
    public Result<CustomerVO> getById(@ApiParam("客户ID") @PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    /**
     * 新增客户
     */
    @ApiOperation("新增客户")
    @PreAuthorize("hasAuthority('customer:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.add(customerDTO);
        return Result.success();
    }

    /**
     * 修改客户
     */
    @ApiOperation("修改客户")
    @PreAuthorize("hasAuthority('customer:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.update(customerDTO);
        return Result.success();
    }

    /**
     * 删除客户
     */
    @ApiOperation("删除客户")
    @PreAuthorize("hasAuthority('customer:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("客户ID") @PathVariable Long id) {
        customerService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除客户
     */
    @ApiOperation("批量删除客户")
    @PreAuthorize("hasAuthority('customer:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("客户ID列表") @RequestBody List<Long> ids) {
        customerService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 启用/禁用客户
     */
    @ApiOperation("启用/禁用客户")
    @PreAuthorize("hasAuthority('customer:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("客户ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        customerService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 客户流失标记
     */
    @ApiOperation("客户流失标记")
    @PreAuthorize("hasAuthority('customer:edit')")
    @PutMapping("/loss/{id}")
    public Result<?> markLoss(@ApiParam("客户ID") @PathVariable Long id, @ApiParam("流失原因") @RequestParam String lossReason) {
        customerService.markLoss(id, lossReason);
        return Result.success();
    }

    /**
     * 客户恢复
     */
    @ApiOperation("客户恢复")
    @PreAuthorize("hasAuthority('customer:edit')")
    @PutMapping("/recover/{id}")
    public Result<?> recover(@ApiParam("客户ID") @PathVariable Long id) {
        customerService.recover(id);
        return Result.success();
    }

    /**
     * 导入客户
     */
    @ApiOperation("导入客户")
    @PreAuthorize("hasAuthority('customer:add')")
    @PostMapping("/import")
    public Result<?> importExcel(@ApiParam("Excel文件") @RequestPart("file") MultipartFile file) {
        customerService.importExcel(file);
        return Result.success();
    }

    /**
     * 导出客户
     */
    @ApiOperation("导出客户")
    @PreAuthorize("hasAuthority('customer:list')")
    @GetMapping("/export")
    public void exportExcel(
            HttpServletResponse response,
            @ApiParam("客户名称") @RequestParam(required = false) String customerName,
            @ApiParam("联系电话") @RequestParam(required = false) String phone,
            @ApiParam("客户类型") @RequestParam(required = false) Integer customerType,
            @ApiParam("负责人ID") @RequestParam(required = false) Long ownerId,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId
    ) {
        customerService.exportExcel(response, customerName, phone, customerType, ownerId, deptId);
    }

    /**
     * 获取客户下拉列表
     */
    @ApiOperation("获取客户下拉列表")
    @PreAuthorize("hasAuthority('customer:query')")
    @GetMapping("/select")
    public Result<List<CustomerVO>> getCustomerSelect() {
        return Result.success(customerService.getCustomerSelect());
    }

    /**
     * 获取客户统计数据
     */
    @ApiOperation("获取客户统计数据")
    @PreAuthorize("hasAuthority('customer:list')")
    @GetMapping("/stats")
    public Result<List<CustomerVO>> getCustomerStats() {
        return Result.success(customerService.getCustomerStats());
    }
}
