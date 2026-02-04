package com.crm.controller.sys;

import com.crm.common.Result;
import com.crm.dto.sys.SysUserDTO;
import com.crm.service.sys.SysUserService;
import com.crm.vo.sys.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统用户控制器
 */
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody SysUserDTO loginDTO) {
        return Result.success(sysUserService.login(loginDTO));
    }

    /**
     * 获取当前用户信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("/current")
    public Result<SysUserVO> getCurrentUser() {
        return Result.success(sysUserService.getCurrentUser());
    }

    /**
     * 用户分页查询
     */
    @ApiOperation("用户分页查询")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("用户名") @RequestParam(required = false) String username,
            @ApiParam("姓名") @RequestParam(required = false) String realName,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        return Result.success(sysUserService.list(pageNum, pageSize, username, realName, deptId, status));
    }

    /**
     * 获取用户详情
     */
    @ApiOperation("获取用户详情")
    @PreAuthorize("hasAuthority('sys:user:query')")
    @GetMapping("/{id}")
    public Result<SysUserVO> getById(@ApiParam("用户ID") @PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody SysUserDTO userDTO) {
        sysUserService.add(userDTO);
        return Result.success();
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody SysUserDTO userDTO) {
        sysUserService.update(userDTO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("用户ID") @PathVariable Long id) {
        sysUserService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @ApiOperation("批量删除用户")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("用户ID列表") @RequestBody List<Long> ids) {
        sysUserService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PutMapping("/resetPassword/{id}")
    public Result<?> resetPassword(@ApiParam("用户ID") @PathVariable Long id) {
        sysUserService.resetPassword(id);
        return Result.success();
    }

    /**
     * 启用/禁用用户
     */
    @ApiOperation("启用/禁用用户")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("用户ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 获取用户角色
     */
    @ApiOperation("获取用户角色")
    @PreAuthorize("hasAuthority('sys:user:query')")
    @GetMapping("/roles/{id}")
    public Result<List<Long>> getUserRoles(@ApiParam("用户ID") @PathVariable Long id) {
        return Result.success(sysUserService.getUserRoles(id));
    }

    /**
     * 分配角色
     */
    @ApiOperation("分配角色")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PutMapping("/roles/{id}")
    public Result<?> assignRoles(@ApiParam("用户ID") @PathVariable Long id, @ApiParam("角色ID列表") @RequestBody List<Long> roleIds) {
        sysUserService.assignRoles(id, roleIds);
        return Result.success();
    }
}
