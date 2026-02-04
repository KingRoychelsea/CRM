package com.crm.controller.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.dto.sales.BusinessOpportunityDTO;
import com.crm.service.sales.BusinessOpportunityService;
import com.crm.vo.sales.BusinessOpportunityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商机信息控制器
 */
@Api(tags = "商机管理")
@RestController
@RequestMapping("/api/sales/opportunity")
public class BusinessOpportunityController {

    @Autowired
    private BusinessOpportunityService businessOpportunityService;

    /**
     * 商机分页查询
     */
    @ApiOperation("商机分页查询")
    @GetMapping("/list")
    public Result<Page<BusinessOpportunityVO>> list(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String opportunityName,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Integer stage,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Page<BusinessOpportunityVO> page = businessOpportunityService.list(pageNum, pageSize, opportunityName, customerId, stage, ownerId, deptId, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取商机详情
     */
    @ApiOperation("获取商机详情")
    @GetMapping("/get/{id}")
    public Result<BusinessOpportunityVO> getById(@PathVariable Long id) {
        BusinessOpportunityVO opportunityVO = businessOpportunityService.getById(id);
        return Result.success(opportunityVO);
    }

    /**
     * 新增商机
     */
    @ApiOperation("新增商机")
    @PostMapping("/add")
    public Result<?> add(@RequestBody BusinessOpportunityDTO opportunityDTO) {
        businessOpportunityService.add(opportunityDTO);
        return Result.success();
    }

    /**
     * 修改商机
     */
    @ApiOperation("修改商机")
    @PutMapping("/update")
    public Result<?> update(@RequestBody BusinessOpportunityDTO opportunityDTO) {
        businessOpportunityService.update(opportunityDTO);
        return Result.success();
    }

    /**
     * 删除商机
     */
    @ApiOperation("删除商机")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        businessOpportunityService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除商机
     */
    @ApiOperation("批量删除商机")
    @DeleteMapping("/batchDelete")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        businessOpportunityService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 更新商机阶段
     */
    @ApiOperation("更新商机阶段")
    @PutMapping("/updateStage")
    public Result<?> updateStage(@RequestParam Long id, @RequestParam Integer stage, @RequestParam(required = false) String failReason) {
        businessOpportunityService.updateStage(id, stage, failReason);
        return Result.success();
    }

    /**
     * 关闭商机
     */
    @ApiOperation("关闭商机")
    @PutMapping("/close")
    public Result<?> close(@RequestParam Long id, @RequestParam String closeReason) {
        // 使用updateStage方法实现关闭商机（阶段设为失败）
        businessOpportunityService.updateStage(id, 6, closeReason);
        return Result.success();
    }

    /**
     * 导出商机
     */
    @ApiOperation("导出商机")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(required = false) String opportunityName,
                            @RequestParam(required = false) Long customerId,
                            @RequestParam(required = false) Integer stage,
                            @RequestParam(required = false) Long ownerId,
                            @RequestParam(required = false) Long deptId,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        businessOpportunityService.exportExcel(response, "商机列表", customerId, stage, ownerId, deptId, startTime, endTime);
    }

    /**
     * 获取商机下拉列表
     */
    @ApiOperation("获取商机下拉列表")
    @GetMapping("/select")
    public Result<List<BusinessOpportunityVO>> getOpportunitySelect() {
        List<BusinessOpportunityVO> opportunityVOs = businessOpportunityService.getOpportunitySelect();
        return Result.success(opportunityVOs);
    }

    /**
     * 获取商机统计数据
     */
    @ApiOperation("获取商机统计数据")
    @GetMapping("/stats")
    public Result<List<BusinessOpportunityVO>> getOpportunityStats() {
        List<BusinessOpportunityVO> opportunityVOs = businessOpportunityService.getOpportunityStats();
        return Result.success(opportunityVOs);
    }
}
