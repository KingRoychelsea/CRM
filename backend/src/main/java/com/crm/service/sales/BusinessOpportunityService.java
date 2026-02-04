package com.crm.service.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.BusinessOpportunityDTO;
import com.crm.vo.sales.BusinessOpportunityVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商机信息服务接口
 */
public interface BusinessOpportunityService {

    /**
     * 商机分页查询
     */
    Page<BusinessOpportunityVO> list(Integer pageNum, Integer pageSize, String opportunityName, Long customerId, Integer stage, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取商机详情
     */
    BusinessOpportunityVO getById(Long id);

    /**
     * 新增商机
     */
    void add(BusinessOpportunityDTO businessOpportunityDTO);

    /**
     * 修改商机
     */
    void update(BusinessOpportunityDTO businessOpportunityDTO);

    /**
     * 删除商机
     */
    void delete(Long id);

    /**
     * 批量删除商机
     */
    void batchDelete(List<Long> ids);

    /**
     * 启用/禁用商机
     */
    void updateStatus(Long id, Integer status);

    /**
     * 商机阶段更新
     */
    void updateStage(Long id, Integer stage, String failReason);

    /**
     * 获取商机下拉列表
     */
    List<BusinessOpportunityVO> getOpportunitySelect();

    /**
     * 获取商机统计数据
     */
    List<BusinessOpportunityVO> getOpportunityStats();

    /**
     * 导出商机Excel
     */
    void exportExcel(HttpServletResponse response, String fileName, Long customerId, Integer stage, Long ownerId, Long deptId, String startTime, String endTime);
}
