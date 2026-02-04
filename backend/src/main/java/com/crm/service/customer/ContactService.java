package com.crm.service.customer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.ContactDTO;
import com.crm.vo.customer.ContactVO;

import java.util.List;

/**
 * 联系人信息服务接口
 */
public interface ContactService {

    /**
     * 联系人分页查询
     */
    Page<ContactVO> list(Integer pageNum, Integer pageSize, Long customerId, String contactName, String phone, Integer priority);

    /**
     * 获取联系人详情
     */
    ContactVO getById(Long id);

    /**
     * 新增联系人
     */
    void add(ContactDTO contactDTO);

    /**
     * 修改联系人
     */
    void update(ContactDTO contactDTO);

    /**
     * 删除联系人
     */
    void delete(Long id);

    /**
     * 批量删除联系人
     */
    void batchDelete(List<Long> ids);

    /**
     * 启用/禁用联系人
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据客户ID获取联系人列表
     */
    List<ContactVO> listByCustomerId(Long customerId);

    /**
     * 获取联系人下拉列表
     */
    List<ContactVO> getContactSelect(Long customerId);
}
