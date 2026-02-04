package com.crm.service.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.PaymentDTO;
import com.crm.vo.sales.PaymentVO;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 回款信息服务接口
 */
public interface PaymentService {

    /**
     * 回款分页查询
     */
    Page<PaymentVO> list(Integer pageNum, Integer pageSize, String paymentName, Long customerId, Long orderId, Long contractId, Integer status, Integer paymentMethod, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取回款详情
     */
    PaymentVO getById(Long id);

    /**
     * 新增回款
     */
    void add(PaymentDTO paymentDTO);

    /**
     * 修改回款
     */
    void update(PaymentDTO paymentDTO);

    /**
     * 删除回款
     */
    void delete(Long id);

    /**
     * 批量删除回款
     */
    void batchDelete(List<Long> ids);

    /**
     * 更新回款状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 导出回款
     */
    void exportExcel(HttpServletResponse response, String paymentName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取未回款提醒
     */
    List<PaymentVO> getPaymentRemind();

    /**
     * 获取回款统计数据
     */
    List<PaymentVO> getPaymentStats();
}
