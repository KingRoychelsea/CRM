package com.crm.service.sales;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.OrderDTO;
import com.crm.vo.sales.OrderVO;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单信息服务接口
 */
public interface OrderService {

    /**
     * 订单分页查询
     */
    Page<OrderVO> list(Integer pageNum, Integer pageSize, String orderName, Long customerId, Long contractId, Integer status, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取订单详情
     */
    OrderVO getById(Long id);

    /**
     * 新增订单
     */
    void add(OrderDTO orderDTO);

    /**
     * 修改订单
     */
    void update(OrderDTO orderDTO);

    /**
     * 删除订单
     */
    void delete(Long id);

    /**
     * 批量删除订单
     */
    void batchDelete(List<Long> ids);

    /**
     * 更新订单状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 导出订单
     */
    void exportExcel(HttpServletResponse response, String orderName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime);

    /**
     * 获取订单下拉列表
     */
    List<OrderVO> getOrderSelect();

    /**
     * 获取订单统计数据
     */
    List<OrderVO> getOrderStats();
}
