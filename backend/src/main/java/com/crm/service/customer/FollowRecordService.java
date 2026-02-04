package com.crm.service.customer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.FollowRecordDTO;
import com.crm.vo.customer.FollowRecordVO;

import java.util.List;

/**
 * 客户跟进记录服务接口
 */
public interface FollowRecordService {

    /**
     * 跟进记录分页查询
     */
    Page<FollowRecordVO> list(Integer pageNum, Integer pageSize, Long customerId, Long contactId, Integer followType, Long followUserId, String startTime, String endTime);

    /**
     * 获取跟进记录详情
     */
    FollowRecordVO getById(Long id);

    /**
     * 新增跟进记录
     */
    void add(FollowRecordDTO followRecordDTO);

    /**
     * 修改跟进记录
     */
    void update(FollowRecordDTO followRecordDTO);

    /**
     * 删除跟进记录
     */
    void delete(Long id);

    /**
     * 批量删除跟进记录
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据客户ID获取跟进记录列表
     */
    List<FollowRecordVO> listByCustomerId(Long customerId);

    /**
     * 获取待跟进提醒列表
     */
    List<FollowRecordVO> getFollowRemind();
}
