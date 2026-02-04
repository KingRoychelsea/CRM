package com.crm.controller.customer;

import com.crm.common.Result;
import com.crm.dto.customer.ContactDTO;
import com.crm.service.customer.ContactService;
import com.crm.vo.customer.ContactVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 联系人信息控制器
 */
@Api(tags = "联系人管理")
@RestController
@RequestMapping("/api/customer/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * 联系人分页查询
     */
    @ApiOperation("联系人分页查询")
    @PreAuthorize("hasAuthority('customer:contact:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("客户ID") @RequestParam(required = false) Long customerId,
            @ApiParam("联系人姓名") @RequestParam(required = false) String contactName,
            @ApiParam("联系电话") @RequestParam(required = false) String phone,
            @ApiParam("优先级") @RequestParam(required = false) Integer priority
    ) {
        return Result.success(contactService.list(pageNum, pageSize, customerId, contactName, phone, priority));
    }

    /**
     * 获取联系人详情
     */
    @ApiOperation("获取联系人详情")
    @PreAuthorize("hasAuthority('customer:contact:query')")
    @GetMapping("/{id}")
    public Result<ContactVO> getById(@ApiParam("联系人ID") @PathVariable Long id) {
        return Result.success(contactService.getById(id));
    }

    /**
     * 新增联系人
     */
    @ApiOperation("新增联系人")
    @PreAuthorize("hasAuthority('customer:contact:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody ContactDTO contactDTO) {
        contactService.add(contactDTO);
        return Result.success();
    }

    /**
     * 修改联系人
     */
    @ApiOperation("修改联系人")
    @PreAuthorize("hasAuthority('customer:contact:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody ContactDTO contactDTO) {
        contactService.update(contactDTO);
        return Result.success();
    }

    /**
     * 删除联系人
     */
    @ApiOperation("删除联系人")
    @PreAuthorize("hasAuthority('customer:contact:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("联系人ID") @PathVariable Long id) {
        contactService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除联系人
     */
    @ApiOperation("批量删除联系人")
    @PreAuthorize("hasAuthority('customer:contact:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("联系人ID列表") @RequestBody List<Long> ids) {
        contactService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 启用/禁用联系人
     */
    @ApiOperation("启用/禁用联系人")
    @PreAuthorize("hasAuthority('customer:contact:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("联系人ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        contactService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据客户ID获取联系人列表
     */
    @ApiOperation("根据客户ID获取联系人列表")
    @PreAuthorize("hasAuthority('customer:contact:query')")
    @GetMapping("/byCustomer/{customerId}")
    public Result<List<ContactVO>> listByCustomerId(@ApiParam("客户ID") @PathVariable Long customerId) {
        return Result.success(contactService.listByCustomerId(customerId));
    }

    /**
     * 获取联系人下拉列表
     */
    @ApiOperation("获取联系人下拉列表")
    @PreAuthorize("hasAuthority('customer:contact:query')")
    @GetMapping("/select")
    public Result<List<ContactVO>> getContactSelect(@ApiParam("客户ID") @RequestParam(required = false) Long customerId) {
        return Result.success(contactService.getContactSelect(customerId));
    }
}
