package com.crm.controller.customer;

import com.crm.common.Result;
import com.crm.dto.customer.FollowRecordDTO;
import com.crm.service.customer.FollowRecordService;
import com.crm.vo.customer.FollowRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户跟进记录控制器
 */
@Api(tags = "跟进记录管理")
@RestController
@RequestMapping("/api/customer/follow")
public class FollowRecordController {

    @Autowired
    private FollowRecordService followRecordService;

    /**
     * 跟进记录分页查询
     */
    @ApiOperation("跟进记录分页查询")
    @PreAuthorize("hasAuthority('customer:follow:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("客户ID") @RequestParam(required = false) Long customerId,
            @ApiParam("联系人ID") @RequestParam(required = false) Long contactId,
            @ApiParam("跟进方式") @RequestParam(required = false) Integer followType,
            @ApiParam("跟进人ID") @RequestParam(required = false) Long followUserId,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime
    ) {
        return Result.success(followRecordService.list(pageNum, pageSize, customerId, contactId, followType, followUserId, startTime, endTime));
    }

    /**
     * 获取跟进记录详情
     */
    @ApiOperation("获取跟进记录详情")
    @PreAuthorize("hasAuthority('customer:follow:query')")
    @GetMapping("/{id}")
    public Result<FollowRecordVO> getById(@ApiParam("跟进记录ID") @PathVariable Long id) {
        return Result.success(followRecordService.getById(id));
    }

    /**
     * 新增跟进记录
     */
    @ApiOperation("新增跟进记录")
    @PreAuthorize("hasAuthority('customer:follow:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody FollowRecordDTO followRecordDTO) {
        followRecordService.add(followRecordDTO);
        return Result.success();
    }

    /**
     * 修改跟进记录
     */
    @ApiOperation("修改跟进记录")
    @PreAuthorize("hasAuthority('customer:follow:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody FollowRecordDTO followRecordDTO) {
        followRecordService.update(followRecordDTO);
        return Result.success();
    }

    /**
     * 删除跟进记录
     */
    @ApiOperation("删除跟进记录")
    @PreAuthorize("hasAuthority('customer:follow:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("跟进记录ID") @PathVariable Long id) {
        followRecordService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除跟进记录
     */
    @ApiOperation("批量删除跟进记录")
    @PreAuthorize("hasAuthority('customer:follow:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("跟进记录ID列表") @RequestBody List<Long> ids) {
        followRecordService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 根据客户ID获取跟进记录列表
     */
    @ApiOperation("根据客户ID获取跟进记录列表")
    @PreAuthorize("hasAuthority('customer:follow:query')")
    @GetMapping("/byCustomer/{customerId}")
    public Result<List<FollowRecordVO>> listByCustomerId(@ApiParam("客户ID") @PathVariable Long customerId) {
        return Result.success(followRecordService.listByCustomerId(customerId));
    }

    /**
     * 获取待跟进提醒列表
     */
    @ApiOperation("获取待跟进提醒列表")
    @PreAuthorize("hasAuthority('customer:follow:list')")
    @GetMapping("/remind")
    public Result<List<FollowRecordVO>> getFollowRemind() {
        return Result.success(followRecordService.getFollowRemind());
    }
}
