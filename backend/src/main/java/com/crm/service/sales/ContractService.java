package com.crm.service.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.ContractDTO;
import com.crm.vo.sales.ContractVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 合同信息服务接口
 */
public interface ContractService {

    /**
     * 合同分页查询
     */
    Page<ContractVO> list(Integer pageNum, Integer pageSize, String contractName, Long customerId, Long opportunityId, Integer status, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取合同详情
     */
    ContractVO getById(Long id);

    /**
     * 新增合同
     */
    void add(ContractDTO contractDTO);

    /**
     * 修改合同
     */
    void update(ContractDTO contractDTO);

    /**
     * 删除合同
     */
    void delete(Long id);

    /**
     * 批量删除合同
     */
    void batchDelete(List<Long> ids);

    /**
     * 更新合同状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 上传合同附件
     */
    String uploadAttachment(Long contractId, MultipartFile file);

    /**
     * 下载合同附件
     */
    void downloadAttachment(Long id, HttpServletResponse response);

    /**
     * 导出合同
     */
    void exportExcel(HttpServletResponse response, String contractName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取合同下拉列表
     */
    List<ContractVO> getContractSelect();

    /**
     * 获取合同统计数据
     */
    List<ContractVO> getContractStats();
}
