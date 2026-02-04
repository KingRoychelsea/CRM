package com.crm.service.impl.sales;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.dto.sales.PaymentDTO;
import com.crm.entity.sales.Payment;
import com.crm.exception.BusinessException;
import com.crm.mapper.sales.PaymentMapper;
import com.crm.service.sales.PaymentService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.sales.PaymentVO;
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
 * 回款信息服务实现
 */
@Slf4j
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    /**
     * 回款分页查询
     */
    @Override
    public Page<PaymentVO> list(Integer pageNum, Integer pageSize, String paymentName, Long customerId, Long orderId, Long contractId, Integer status, Integer paymentMethod, Long ownerId, Long deptId, String startTime, String endTime) {
        Page<Payment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getDelFlag, 0);
        
        // 条件查询
        if (StringUtils.hasText(paymentName)) {
            wrapper.like(Payment::getPaymentName, paymentName);
        }
        if (customerId != null) {
            wrapper.eq(Payment::getCustomerId, customerId);
        }
        if (orderId != null) {
            wrapper.eq(Payment::getOrderId, orderId);
        }
        if (contractId != null) {
            wrapper.eq(Payment::getContractId, contractId);
        }
        if (status != null) {
            wrapper.eq(Payment::getStatus, status);
        }
        if (paymentMethod != null) {
            wrapper.eq(Payment::getPaymentMethod, paymentMethod);
        }
        if (ownerId != null) {
            wrapper.eq(Payment::getOwnerId, ownerId);
        }
        if (deptId != null) {
            wrapper.eq(Payment::getDeptId, deptId);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(Payment::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(Payment::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Payment::getCreateTime);
        Page<Payment> paymentPage = baseMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<PaymentVO> resultPage = new Page<>();
        resultPage.setTotal(paymentPage.getTotal());
        resultPage.setCurrent(paymentPage.getCurrent());
        resultPage.setSize(paymentPage.getSize());
        
        List<PaymentVO> paymentVOs = new ArrayList<>();
        for (Payment payment : paymentPage.getRecords()) {
            PaymentVO paymentVO = new PaymentVO();
            BeanUtils.copyProperties(payment, paymentVO);
            paymentVOs.add(paymentVO);
        }
        resultPage.setRecords(paymentVOs);
        
        return resultPage;
    }

    /**
     * 获取回款详情
     */
    @Override
    public PaymentVO getById(Long id) {
        Payment payment = baseMapper.selectById(id);
        if (payment == null || payment.getDelFlag() == 1) {
            throw new BusinessException("回款不存在");
        }
        PaymentVO paymentVO = new PaymentVO();
        BeanUtils.copyProperties(payment, paymentVO);
        return paymentVO;
    }

    /**
     * 新增回款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PaymentDTO paymentDTO) {
        // 检查回款名称是否重复
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentName, paymentDTO.getPaymentName());
        wrapper.eq(Payment::getDelFlag, 0);
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("回款名称已存在");
        }
        
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDTO, payment);
        
        // 设置创建信息
        String currentUserId = String.valueOf(SecurityUtils.getCurrentUserId());
        payment.setCreateBy(currentUserId);
        payment.setCreateTime(LocalDateTime.now());
        payment.setUpdateBy(currentUserId);
        payment.setUpdateTime(LocalDateTime.now());
        payment.setDelFlag(0);
        
        // 设置默认状态
        payment.setStatus(0); // 待回款
        
        baseMapper.insert(payment);
    }

    /**
     * 修改回款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PaymentDTO paymentDTO) {
        Payment payment = baseMapper.selectById(paymentDTO.getId());
        if (payment == null || payment.getDelFlag() == 1) {
            throw new BusinessException("回款不存在");
        }
        
        // 检查回款名称是否重复
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentName, paymentDTO.getPaymentName());
        wrapper.ne(Payment::getId, paymentDTO.getId());
        wrapper.eq(Payment::getDelFlag, 0);
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("回款名称已存在");
        }
        
        BeanUtils.copyProperties(paymentDTO, payment);
        
        // 更新信息
        payment.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        payment.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(payment);
    }

    /**
     * 删除回款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Payment payment = baseMapper.selectById(id);
        if (payment == null || payment.getDelFlag() == 1) {
            throw new BusinessException("回款不存在");
        }
        
        // 逻辑删除
        payment.setDelFlag(1);
        payment.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        payment.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(payment);
    }

    /**
     * 批量删除回款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    /**
     * 更新回款状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Payment payment = baseMapper.selectById(id);
        if (payment == null || payment.getDelFlag() == 1) {
            throw new BusinessException("回款不存在");
        }
        
        payment.setStatus(status);
        payment.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        payment.setUpdateTime(LocalDateTime.now());
        
        baseMapper.updateById(payment);
    }

    /**
     * 导出回款
     */
    @Override
    public void exportExcel(HttpServletResponse response, String paymentName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime) {
        // 构建查询条件
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getDelFlag, 0);
        
        if (StringUtils.hasText(paymentName)) {
            wrapper.like(Payment::getPaymentName, paymentName);
        }
        if (customerId != null) {
            wrapper.eq(Payment::getCustomerId, customerId);
        }
        if (status != null) {
            wrapper.eq(Payment::getStatus, status);
        }
        if (ownerId != null) {
            wrapper.eq(Payment::getOwnerId, ownerId);
        }
        if (deptId != null) {
            wrapper.eq(Payment::getDeptId, deptId);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(Payment::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(Payment::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Payment::getCreateTime);
        
        List<Payment> payments = baseMapper.selectList(wrapper);
        
        // 这里实现Excel导出逻辑
        // 由于需要引入POI依赖，这里仅做示例
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("回款数据", "UTF-8") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            
            OutputStream outputStream = response.getOutputStream();
            // 实际项目中使用POI生成Excel并写入outputStream
            outputStream.write("回款数据导出示例".getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("导出回款失败", e);
            throw new BusinessException("导出回款失败");
        }
    }

    /**
     * 获取未回款提醒
     */
    @Override
    public List<PaymentVO> getPaymentRemind() {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getDelFlag, 0);
        wrapper.eq(Payment::getStatus, 0); // 待回款
        wrapper.lt(Payment::getPlanDate, LocalDateTime.now()); // 计划回款日期已过期
        wrapper.orderByAsc(Payment::getPlanDate);
        
        List<Payment> payments = baseMapper.selectList(wrapper);
        
        List<PaymentVO> paymentVOs = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentVO paymentVO = new PaymentVO();
            BeanUtils.copyProperties(payment, paymentVO);
            paymentVOs.add(paymentVO);
        }
        
        return paymentVOs;
    }

    /**
     * 获取回款统计数据
     */
    @Override
    public List<PaymentVO> getPaymentStats() {
        // 这里实现回款统计逻辑
        // 实际项目中可能需要使用group by查询
        return new ArrayList<>();
    }
}
