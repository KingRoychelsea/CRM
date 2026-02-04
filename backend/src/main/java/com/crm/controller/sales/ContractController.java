package com.crm.controller.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.dto.sales.ContractDTO;
import com.crm.service.sales.ContractService;
import com.crm.vo.sales.ContractVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 合同信息控制器
 */
@Api(tags = "合同管理")
@RestController
@RequestMapping("/api/sales/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * 合同分页查询
     */
    @ApiOperation("合同分页查询")
    @GetMapping("/list")
    public Result<Page<ContractVO>> list(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long opportunityId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Page<ContractVO> page = contractService.list(pageNum, pageSize, contractName, customerId, opportunityId, status, ownerId, deptId, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取合同详情
     */
    @ApiOperation("获取合同详情")
    @GetMapping("/get/{id}")
    public Result<ContractVO> getById(@PathVariable Long id) {
        ContractVO contractVO = contractService.getById(id);
        return Result.success(contractVO);
    }

    /**
     * 新增合同
     */
    @ApiOperation("新增合同")
    @PostMapping("/add")
    public Result<?> add(@RequestBody ContractDTO contractDTO) {
        contractService.add(contractDTO);
        return Result.success();
    }

    /**
     * 修改合同
     */
    @ApiOperation("修改合同")
    @PutMapping("/update")
    public Result<?> update(@RequestBody ContractDTO contractDTO) {
        contractService.update(contractDTO);
        return Result.success();
    }

    /**
     * 删除合同
     */
    @ApiOperation("删除合同")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        contractService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除合同
     */
    @ApiOperation("批量删除合同")
    @DeleteMapping("/batchDelete")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        contractService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 更新合同状态
     */
    @ApiOperation("更新合同状态")
    @PutMapping("/updateStatus")
    public Result<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        contractService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 上传合同附件
     */
    @ApiOperation("上传合同附件")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam Long contractId, @RequestParam MultipartFile file) {
        String filePath = contractService.uploadAttachment(contractId, file);
        return Result.success(filePath);
    }

    /**
     * 下载合同附件
     */
    @ApiOperation("下载合同附件")
    @GetMapping("/download")
    public void download(@RequestParam Long contractId, HttpServletResponse response) {
        contractService.downloadAttachment(contractId, response);
    }

    /**
     * 导出合同
     */
    @ApiOperation("导出合同")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(required = false) String contractName,
                            @RequestParam(required = false) Long customerId,
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) Long ownerId,
                            @RequestParam(required = false) Long deptId,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {
        contractService.exportExcel(response, contractName, customerId, status, ownerId, deptId, startTime, endTime);
    }

    /**
     * 获取合同下拉列表
     */
    @ApiOperation("获取合同下拉列表")
    @GetMapping("/select")
    public Result<List<ContractVO>> getContractSelect() {
        List<ContractVO> contractVOs = contractService.getContractSelect();
        return Result.success(contractVOs);
    }

    /**
     * 获取合同统计数据
     */
    @ApiOperation("获取合同统计数据")
    @GetMapping("/stats")
    public Result<List<ContractVO>> getContractStats() {
        List<ContractVO> contractVOs = contractService.getContractStats();
        return Result.success(contractVOs);
    }
}
