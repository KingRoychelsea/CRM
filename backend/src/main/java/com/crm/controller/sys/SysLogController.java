package com.crm.controller.sys;

import com.crm.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志控制器
 */
@Api(tags = "系统日志管理")
@RestController
@RequestMapping("/api/sys/log")
public class SysLogController {

    @Autowired
    private com.crm.service.sys.SysOperLogService sysOperLogService;

    @Autowired
    private com.crm.service.sys.SysLoginLogService sysLoginLogService;

    /**
     * 操作日志分页查询
     */
    @ApiOperation("操作日志分页查询")
    @PreAuthorize("hasAuthority('sys:log:list')")
    @GetMapping("/oper/list")
    public Result<?> listOper(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("操作用户") @RequestParam(required = false) String operUser,
            @ApiParam("操作类型") @RequestParam(required = false) String operType,
            @ApiParam("操作模块") @RequestParam(required = false) String operModule,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime
    ) {
        return Result.success(sysOperLogService.list(pageNum, pageSize, operUser, operType, operModule, startTime, endTime));
    }

    /**
     * 获取操作日志详情
     */
    @ApiOperation("获取操作日志详情")
    @PreAuthorize("hasAuthority('sys:log:query')")
    @GetMapping("/oper/{id}")
    public Result<?> getOperById(@ApiParam("操作日志ID") @PathVariable Long id) {
        return Result.success(sysOperLogService.getById(id));
    }

    /**
     * 删除操作日志
     */
    @ApiOperation("删除操作日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/oper/{id}")
    public Result<?> deleteOper(@ApiParam("操作日志ID") @PathVariable Long id) {
        sysOperLogService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除操作日志
     */
    @ApiOperation("批量删除操作日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/oper/batch")
    public Result<?> batchDeleteOper(@ApiParam("操作日志ID列表") @RequestBody List<Long> ids) {
        sysOperLogService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 清空操作日志
     */
    @ApiOperation("清空操作日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/oper/clear")
    public Result<?> clearOper() {
        sysOperLogService.clear();
        return Result.success();
    }

    /**
     * 登录日志分页查询
     */
    @ApiOperation("登录日志分页查询")
    @PreAuthorize("hasAuthority('sys:log:list')")
    @GetMapping("/login/list")
    public Result<?> listLogin(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("登录用户") @RequestParam(required = false) String loginUser,
            @ApiParam("登录状态") @RequestParam(required = false) Integer status,
            @ApiParam("登录IP") @RequestParam(required = false) String loginIp,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime
    ) {
        return Result.success(sysLoginLogService.list(pageNum, pageSize, loginUser, status, loginIp, startTime, endTime));
    }

    /**
     * 获取登录日志详情
     */
    @ApiOperation("获取登录日志详情")
    @PreAuthorize("hasAuthority('sys:log:query')")
    @GetMapping("/login/{id}")
    public Result<?> getLoginById(@ApiParam("登录日志ID") @PathVariable Long id) {
        return Result.success(sysLoginLogService.getById(id));
    }

    /**
     * 删除登录日志
     */
    @ApiOperation("删除登录日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/login/{id}")
    public Result<?> deleteLogin(@ApiParam("登录日志ID") @PathVariable Long id) {
        sysLoginLogService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除登录日志
     */
    @ApiOperation("批量删除登录日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/login/batch")
    public Result<?> batchDeleteLogin(@ApiParam("登录日志ID列表") @RequestBody List<Long> ids) {
        sysLoginLogService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 清空登录日志
     */
    @ApiOperation("清空登录日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @DeleteMapping("/login/clear")
    public Result<?> clearLogin() {
        sysLoginLogService.clear();
        return Result.success();
    }
}
