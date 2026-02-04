package com.crm.controller.sys;

import com.crm.common.Result;
import com.crm.config.ApiPathConfig;
import com.crm.dto.sys.SysDeptDTO;
import com.crm.entity.sys.SysDept;
import com.crm.service.sys.SysDeptService;
import com.crm.vo.sys.SysDeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统部门控制器
 */
@Api(tags = "系统部门管理")
@RestController
@RequestMapping(ApiPathConfig.DEPT_PATH)
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 获取部门树形结构
     */
    @ApiOperation("获取部门树形结构")
    @PreAuthorize("hasAuthority('sys:dept:list')")
    @GetMapping("/tree")
    public Result<List<SysDeptVO>> getDeptTree() {
        return Result.success(sysDeptService.getDeptTree());
    }

    /**
     * 部门分页查询
     */
    @ApiOperation("部门分页查询")
    @PreAuthorize("hasAuthority('sys:dept:list')")
    @GetMapping("/list")
    public Result<?> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("部门名称") @RequestParam(required = false) String deptName,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        SysDeptDTO dto = new SysDeptDTO();
        dto.setDeptName(deptName);
        dto.setStatus(status != null ? status.toString() : null);
        return Result.success(sysDeptService.getDeptList(dto));
    }

    /**
     * 获取部门详情
     */
    @ApiOperation("获取部门详情")
    @PreAuthorize("hasAuthority('sys:dept:query')")
    @GetMapping("/{id}")
    public Result<SysDept> getById(@ApiParam("部门ID") @PathVariable Long id) {
        return Result.success(sysDeptService.getById(id));
    }

    /**
     * 新增部门
     */
    @ApiOperation("新增部门")
    @PreAuthorize("hasAuthority('sys:dept:add')")
    @PostMapping
    public Result<?> add(@Valid @RequestBody SysDeptDTO deptDTO) {
        sysDeptService.add(deptDTO);
        return Result.success();
    }

    /**
     * 修改部门
     */
    @ApiOperation("修改部门")
    @PreAuthorize("hasAuthority('sys:dept:edit')")
    @PutMapping
    public Result<?> update(@Valid @RequestBody SysDeptDTO deptDTO) {
        sysDeptService.update(deptDTO);
        return Result.success();
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam("部门ID") @PathVariable Long id) {
        sysDeptService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除部门
     */
    @ApiOperation("批量删除部门")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@ApiParam("部门ID列表") @RequestBody List<Long> ids) {
        sysDeptService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 启用/禁用部门
     */
    @ApiOperation("启用/禁用部门")
    @PreAuthorize("hasAuthority('sys:dept:edit')")
    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@ApiParam("部门ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        sysDeptService.updateStatus(id, status.toString());
        return Result.success();
    }

    /**
     * 部门排序
     */
    @ApiOperation("部门排序")
    @PreAuthorize("hasAuthority('sys:dept:edit')")
    @PutMapping("/sort")
    public Result<?> sort(@ApiParam("部门排序数据") @RequestBody List<SysDeptDTO> deptDTOs) {
        sysDeptService.sort(deptDTOs);
        return Result.success();
    }

    /**
     * 获取部门下拉树（用于用户分配部门）
     */
    @ApiOperation("获取部门下拉树")
    @PreAuthorize("hasAuthority('sys:dept:query')")
    @GetMapping("/selectTree")
    public Result<List<SysDeptVO>> getSelectTree() {
        return Result.success(sysDeptService.getSelectTree());
    }

    /**
     * 获取部门用户列表
     */
    @ApiOperation("获取部门用户列表")
    @PreAuthorize("hasAuthority('sys:dept:query')")
    @GetMapping("/users/{id}")
    public Result<?> getDeptUsers(
            @ApiParam("部门ID") @PathVariable Long id,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("用户名") @RequestParam(required = false) String username,
            @ApiParam("姓名") @RequestParam(required = false) String realName,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        return Result.success(sysDeptService.getDeptUsers(id, pageNum, pageSize, username, realName, status));
    }
}
