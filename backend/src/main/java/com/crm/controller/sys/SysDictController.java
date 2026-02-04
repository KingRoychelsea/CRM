package com.crm.controller.sys;

import com.crm.common.Result;
import com.crm.dto.sys.SysDictDTO;
import com.crm.dto.sys.SysDictItemDTO;
import com.crm.entity.sys.SysDictItem;
import com.crm.service.sys.SysDictService;
import com.crm.vo.sys.SysDictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统字典控制器
 */
@Api(tags = "系统字典管理")
@RestController
@RequestMapping("/api/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 字典类型分页查询
     */
    @ApiOperation("字典类型分页查询")
    @PreAuthorize("hasAuthority('sys:dict:list')")
    @GetMapping("/type/list")
    public Result<?> listType(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("字典类型") @RequestParam(required = false) String dictType,
            @ApiParam("字典名称") @RequestParam(required = false) String dictName,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ) {
        return Result.success(sysDictService.listType(pageNum, pageSize, dictType, dictName, status));
    }

    /**
     * 获取字典类型详情
     */
    @ApiOperation("获取字典类型详情")
    @PreAuthorize("hasAuthority('sys:dict:query')")
    @GetMapping("/type/{id}")
    public Result<SysDictVO> getTypeById(@ApiParam("字典类型ID") @PathVariable Long id) {
        return Result.success(sysDictService.getTypeById(id));
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @PreAuthorize("hasAuthority('sys:dict:add')")
    @PostMapping("/type")
    public Result<?> addType(@Valid @RequestBody SysDictDTO dictDTO) {
        sysDictService.addType(dictDTO);
        return Result.success();
    }

    /**
     * 修改字典类型
     */
    @ApiOperation("修改字典类型")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    @PutMapping("/type")
    public Result<?> updateType(@Valid @RequestBody SysDictDTO dictDTO) {
        sysDictService.updateType(dictDTO);
        return Result.success();
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @DeleteMapping("/type/{id}")
    public Result<?> deleteType(@ApiParam("字典类型ID") @PathVariable Long id) {
        sysDictService.deleteType(id);
        return Result.success();
    }

    /**
     * 批量删除字典类型
     */
    @ApiOperation("批量删除字典类型")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @DeleteMapping("/type/batch")
    public Result<?> batchDeleteType(@ApiParam("字典类型ID列表") @RequestBody List<Long> ids) {
        sysDictService.batchDeleteType(ids);
        return Result.success();
    }

    /**
     * 启用/禁用字典类型
     */
    @ApiOperation("启用/禁用字典类型")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    @PutMapping("/type/status/{id}")
    public Result<?> updateTypeStatus(@ApiParam("字典类型ID") @PathVariable Long id, @ApiParam("状态") @RequestParam Integer status) {
        sysDictService.updateTypeStatus(id, status);
        return Result.success();
    }

    /**
     * 获取字典项列表（根据字典类型）
     */
    @ApiOperation("获取字典项列表")
    @PreAuthorize("hasAuthority('sys:dict:query')")
    @GetMapping("/item/list/{typeId}")
    public Result<?> listItem(@ApiParam("字典类型ID") @PathVariable Long typeId) {
        return Result.success(sysDictService.listItem(typeId));
    }

    /**
     * 新增字典项
     */
    @ApiOperation("新增字典项")
    @PreAuthorize("hasAuthority('sys:dict:add')")
    @PostMapping("/item")
    public Result<?> addItem(@Valid @RequestBody SysDictItemDTO dictDTO) {
        sysDictService.addItem(dictDTO);
        return Result.success();
    }

    /**
     * 修改字典项
     */
    @ApiOperation("修改字典项")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    @PutMapping("/item")
    public Result<?> updateItem(@Valid @RequestBody SysDictItemDTO dictDTO) {
        sysDictService.updateItem(dictDTO);
        return Result.success();
    }

    /**
     * 删除字典项
     */
    @ApiOperation("删除字典项")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @DeleteMapping("/item/{id}")
    public Result<?> deleteItem(@ApiParam("字典项ID") @PathVariable Long id) {
        sysDictService.deleteItem(id);
        return Result.success();
    }

    /**
     * 批量删除字典项
     */
    @ApiOperation("批量删除字典项")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @DeleteMapping("/item/batch")
    public Result<?> batchDeleteItem(@ApiParam("字典项ID列表") @RequestBody List<Long> ids) {
        sysDictService.batchDeleteItem(ids);
        return Result.success();
    }

    /**
     * 获取字典数据（前端使用）
     */
    @ApiOperation("获取字典数据")
    @GetMapping("/data/{dictType}")
    public Result<List<SysDictItem>> getDictData(@ApiParam("字典类型") @PathVariable String dictType) {
        return Result.success(sysDictService.getDictData(dictType));
    }

    /**
     * 获取所有字典类型（用于选择）
     */
    @ApiOperation("获取所有字典类型")
    @PreAuthorize("hasAuthority('sys:dict:query')")
    @GetMapping("/type/all")
    public Result<List<SysDictVO>> getAllType() {
        return Result.success(sysDictService.getAllType());
    }
}
