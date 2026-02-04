package com.crm.service.customer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.CustomerDTO;
import com.crm.vo.customer.CustomerVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户信息服务接口
 */
public interface CustomerService {

    /**
     * 客户分页查询
     */
    Page<CustomerVO> list(Integer pageNum, Integer pageSize, String customerName, String phone, Integer customerType, Long ownerId, Long deptId);

    /**
     * 获取客户详情
     */
    CustomerVO getById(Long id);

    /**
     * 新增客户
     */
    void add(CustomerDTO customerDTO);

    /**
     * 修改客户
     */
    void update(CustomerDTO customerDTO);

    /**
     * 删除客户
     */
    void delete(Long id);

    /**
     * 批量删除客户
     */
    void batchDelete(List<Long> ids);

    /**
     * 启用/禁用客户
     */
    void updateStatus(Long id, Integer status);

    /**
     * 客户流失标记
     */
    void markLoss(Long id, String lossReason);

    /**
     * 客户恢复
     */
    void recover(Long id);

    /**
     * 导入客户
     */
    void importExcel(MultipartFile file);

    /**
     * 导出客户
     */
    void exportExcel(HttpServletResponse response, String customerName, String phone, Integer customerType, Long ownerId, Long deptId);

    /**
     * 获取客户下拉列表
     */
    List<CustomerVO> getCustomerSelect();

    /**
     * 获取客户统计数据
     */
    List<CustomerVO> getCustomerStats();
}
