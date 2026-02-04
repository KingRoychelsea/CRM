package com.crm.controller.sys;

import com.crm.common.Result;
import com.crm.dto.sys.SysRoleDTO;
import com.crm.entity.sys.SysRole;
import com.crm.service.sys.SysRoleService;
import com.crm.vo.sys.SysRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统角色控制器
 */
@Api(tags = "系统角色管理")
@RestController
@RequestMapping("/api/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 角色分页查询
     */
    @ApiOperation("角色分页查询")
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("角色名称") @RequestParam(required = false) String roleName,
            @ApiParam("角色编码") @RequestParam(required = false) String roleKey,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        SysRoleDTO dto = new SysRoleDTO();
        dto.setRoleName(roleName);
        dto.setRoleKey(roleKey);
        dto.setStatus(status != null ? status.toString() : null);
        return Result.success(sysRoleService.getRoleList(dto));
    }

    /**
     * 获取角色详情
     */
    @ApiOperation("获取角色详情")
    @PreAuthorize("hasAuthority('sys:role:query')")
    @GetMapping("/{id}")
    public Result<SysRole> getById(@ApiParam("角色ID") @PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody SysRoleDTO roleDTO) {
        sysRoleService.add(roleDTO);
        return Result.success();
    }

    /**
     * 修改角色
     */
    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody SysRoleDTO roleDTO) {
        sysRoleService.update(roleDTO);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("角色ID") @PathVariable Long id) {
        sysRoleService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除角色
     */
    @ApiOperation("批量删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("角色ID列表") @RequestBody List<Long> ids) {
        sysRoleService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 启用/禁用角色
     */
    @ApiOperation("启用/禁用角色")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("角色ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        sysRoleService.updateStatus(id, status.toString());
        return Result.success();
    }

    /**
     * 获取角色菜单权限
     */
    @ApiOperation("获取角色菜单权限")
    @PreAuthorize("hasAuthority('sys:role:query')")
    @GetMapping("/menu/{id}")
    public Result<List<Long>> getRoleMenus(@ApiParam("角色ID") @PathVariable Long id) {
        return Result.success(sysRoleService.getRoleMenus(id));
    }

    /**
     * 分配菜单权限
     */
    @ApiOperation("分配菜单权限")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PutMapping("/menu/{id}")
    public Result<?> assignMenus(@ApiParam("角色ID") @PathVariable Long id, @ApiParam("菜单ID列表") @RequestBody List<Long> menuIds) {
        sysRoleService.assignMenus(id, menuIds);
        return Result.success();
    }

    /**
     * 获取所有角色（用于用户分配角色）
     */
    @ApiOperation("获取所有角色")
    @PreAuthorize("hasAuthority('sys:role:query')")
    @GetMapping("/all")
    public Result<List<SysRoleVO>> getAll() {
        return Result.success(sysRoleService.getAll());
    }

    /**
     * 获取角色用户列表
     */
    @ApiOperation("获取角色用户列表")
    @PreAuthorize("hasAuthority('sys:role:query')")
    @GetMapping("/users/{id}")
    public Result<?> getRoleUsers(
            @ApiParam("角色ID") @PathVariable Long id,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("用户名") @RequestParam(required = false) String username,
            @ApiParam("姓名") @RequestParam(required = false) String realName
    ) {
        return Result.success(sysRoleService.getRoleUsers(id, pageNum, pageSize, username, realName));
    }

    /**
     * 批量授权用户
     */
    @ApiOperation("批量授权用户")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PutMapping("/users/{id}")
    public Result<?> assignUsers(@ApiParam("角色ID") @PathVariable Long id, @ApiParam("用户ID列表") @RequestBody List<Long> userIds) {
        sysRoleService.assignUsers(id, userIds);
        return Result.success();
    }
}
