package com.crm.controller.sys;

import com.crm.common.Result;
import com.crm.dto.sys.SysMenuDTO;
import com.crm.entity.sys.SysMenu;
import com.crm.service.sys.SysMenuService;
import com.crm.vo.sys.SysMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统菜单控制器
 */
@Api(tags = "系统菜单管理")
@RestController
@RequestMapping("/api/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 获取菜单树形结构
     */
    @ApiOperation("获取菜单树形结构")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/tree")
    public Result<List<SysMenuVO>> getMenuTree() {
        return Result.success(sysMenuService.getMenuTree());
    }

    /**
     * 获取当前用户菜单（用于前端路由）
     */
    @ApiOperation("获取当前用户菜单")
    @GetMapping("/current")
    public Result<List<SysMenuVO>> getCurrentUserMenu() {
        return Result.success(sysMenuService.getCurrentUserMenu());
    }

    /**
     * 菜单分页查询
     */
    @ApiOperation("菜单分页查询")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("菜单名称") @RequestParam(required = false) String menuName,
            @ApiParam("菜单类型") @RequestParam(required = false) Integer menuType,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        SysMenuDTO dto = new SysMenuDTO();
        dto.setMenuName(menuName);
        dto.setMenuType(menuType != null ? menuType.toString() : null);
        dto.setStatus(status != null ? status.toString() : null);
        return Result.success(sysMenuService.getMenuList(dto));
    }

    /**
     * 获取菜单详情
     */
    @ApiOperation("获取菜单详情")
    @PreAuthorize("hasAuthority('sys:menu:query')")
    @GetMapping("/{id}")
    public Result<SysMenu> getById(@ApiParam("菜单ID") @PathVariable Long id) {
        return Result.success(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     */
    @ApiOperation("新增菜单")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody SysMenuDTO menuDTO) {
        sysMenuService.add(menuDTO);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @ApiOperation("修改菜单")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody SysMenuDTO menuDTO) {
        sysMenuService.update(menuDTO);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("菜单ID") @PathVariable Long id) {
        sysMenuService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除菜单
     */
    @ApiOperation("批量删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("菜单ID列表") @RequestBody List<Long> ids) {
        sysMenuService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 启用/禁用菜单
     */
    @ApiOperation("启用/禁用菜单")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("菜单ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        sysMenuService.updateStatus(id, status.toString());
        return Result.success();
    }

    /**
     * 菜单排序
     */
    @ApiOperation("菜单排序")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @PutMapping("/sort")
    public Result<?> sort(@ApiParam("菜单排序数据") @RequestBody List<SysMenuDTO> menuDTOs) {
        sysMenuService.sort(menuDTOs);
        return Result.success();
    }

    /**
     * 获取菜单下拉树（用于选择父菜单）
     */
    @ApiOperation("获取菜单下拉树")
    @PreAuthorize("hasAuthority('sys:menu:query')")
    @GetMapping("/selectTree")
    public Result<List<SysMenuVO>> getSelectTree() {
        return Result.success(sysMenuService.getSelectTree());
    }
}
