package com.crm.service.impl.sales;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.dto.sales.OrderDTO;
import com.crm.entity.sales.Order;
import com.crm.exception.BusinessException;
import com.crm.mapper.sales.OrderMapper;
import com.crm.service.sales.OrderService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.sales.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单信息服务实现
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    /**
     * 订单分页查询
     */
    @Override
    public Page<OrderVO> list(Integer pageNum, Integer pageSize, String orderName, Long customerId, Long contractId, Integer status, Long ownerId, Long deptId, String startTime, String endTime) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getDelFlag, 0);
        
        // 条件查询
        if (StringUtils.hasText(orderName)) {
            wrapper.like(Order::getOrderName, orderName);
        }
        if (customerId != null) {
            wrapper.eq(Order::getCustomerId, customerId);
        }
        if (contractId != null) {
            wrapper.eq(Order::getContractId, contractId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        if (ownerId != null) {
            wrapper.eq(Order::getOwnerId, ownerId);
        }
        if (deptId != null) {
            wrapper.eq(Order::getDeptId, deptId);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(Order::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(Order::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = baseMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<OrderVO> resultPage = new Page<>();
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setCurrent(orderPage.getCurrent());
        resultPage.setSize(orderPage.getSize());
        
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOs.add(orderVO);
        }
        resultPage.setRecords(orderVOs);
        
        return resultPage;
    }

    /**
     * 获取订单详情
     */
    @Override
    public OrderVO getById(Long id) {
        Order order = baseMapper.selectById(id);
        if (order == null || order.getDelFlag() == 1) {
            throw new BusinessException("订单不存在");
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }

    /**
     * 新增订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(OrderDTO orderDTO) {
        // 检查订单名称是否重复
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderName, orderDTO.getOrderName());
        wrapper.eq(Order::getDelFlag, 0);
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("订单名称已存在");
        }
        
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        
        // 设置创建信息
        String currentUserId = String.valueOf(SecurityUtils.getCurrentUserId());
        order.setCreateBy(currentUserId);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateBy(currentUserId);
        order.setUpdateTime(LocalDateTime.now());
        order.setDelFlag(0);
        
        // 设置默认状态
        order.setStatus(0); // 待付款
        
        baseMapper.insert(order);
    }

    /**
     * 修改订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrderDTO orderDTO) {
        Order order = baseMapper.selectById(orderDTO.getId());
        if (order == null || order.getDelFlag() == 1) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单名称是否重复
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderName, orderDTO.getOrderName());
        wrapper.ne(Order::getId, orderDTO.getId());
        wrapper.eq(Order::getDelFlag, 0);
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("订单名称已存在");
        }
        
        BeanUtils.copyProperties(orderDTO, order);
        
        // 更新信息
        order.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        order.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(order);
    }

    /**
     * 删除订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Order order = baseMapper.selectById(id);
        if (order == null || order.getDelFlag() == 1) {
            throw new BusinessException("订单不存在");
        }
        
        // 逻辑删除
        order.setDelFlag(1);
        order.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        order.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(order);
    }

    /**
     * 批量删除订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    /**
     * 更新订单状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Order order = baseMapper.selectById(id);
        if (order == null || order.getDelFlag() == 1) {
            throw new BusinessException("订单不存在");
        }
        
        order.setStatus(status);
        order.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        order.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(order);
    }

    /**
     * 导出订单
     */
    @Override
    public void exportExcel(HttpServletResponse response, String orderName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime) {
        // 构建查询条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getDelFlag, 0);
        
        if (StringUtils.hasText(orderName)) {
            wrapper.like(Order::getOrderName, orderName);
        }
        if (customerId != null) {
            wrapper.eq(Order::getCustomerId, customerId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        if (ownerId != null) {
            wrapper.eq(Order::getOwnerId, ownerId);
        }
        if (deptId != null) {
            wrapper.eq(Order::getDeptId, deptId);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(Order::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(Order::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        
        List<Order> orders = baseMapper.selectList(wrapper);
        
        // 这里实现Excel导出逻辑
        // 由于需要引入POI依赖，这里仅做示例
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("订单数据", "UTF-8") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            
            OutputStream outputStream = response.getOutputStream();
            // 实际项目中使用POI生成Excel并写入outputStream
            outputStream.write("订单数据导出示例".getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("导出订单失败", e);
            throw new BusinessException("导出订单失败");
        }
    }

    /**
     * 获取订单下拉列表
     */
    @Override
    public List<OrderVO> getOrderSelect() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getDelFlag, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        
        List<Order> orders = baseMapper.selectList(wrapper);
        
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOs.add(orderVO);
        }
        
        return orderVOs;
    }

    /**
     * 获取订单统计数据
     */
    @Override
    public List<OrderVO> getOrderStats() {
        // 这里实现订单统计逻辑
        // 实际项目中可能需要使用group by查询
        return new ArrayList<>();
    }
}
