package com.crm.service.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.CustomerDTO;
import com.crm.entity.customer.Customer;
import com.crm.mapper.customer.CustomerMapper;
import com.crm.service.customer.CustomerService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.customer.CustomerVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户信息服务实现类
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Page<CustomerVO> list(Integer pageNum, Integer pageSize, String customerName, String phone, Integer customerType, Long ownerId, Long deptId) {
        // 构建查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(customerName)) {
            queryWrapper.like(Customer::getCustomerName, customerName);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like(Customer::getPhone, phone);
        }
        if (customerType != null) {
            queryWrapper.eq(Customer::getCustomerType, customerType);
        }
        if (ownerId != null) {
            queryWrapper.eq(Customer::getOwnerId, ownerId);
        }
        if (deptId != null) {
            queryWrapper.eq(Customer::getDeptId, deptId);
        }
        queryWrapper.eq(Customer::getDelFlag, 0);
        queryWrapper.orderByDesc(Customer::getCreateTime);

        // 分页查询
        Page<Customer> page = new Page<>(pageNum, pageSize);
        customerMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<CustomerVO> pageVO = new Page<>();
        BeanUtils.copyProperties(page, pageVO);
        List<CustomerVO> records = new ArrayList<>();
        for (Customer customer : page.getRecords()) {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            // 设置类型名称
            customerVO.setCustomerTypeName(getCustomerTypeName(customer.getCustomerType()));
            // 设置状态名称
            customerVO.setStatusName(customer.getStatus() == 1 ? "启用" : "禁用");
            records.add(customerVO);
        }
        pageVO.setRecords(records);
        return pageVO;
    }

    @Override
    public CustomerVO getById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        // 设置类型名称
        customerVO.setCustomerTypeName(getCustomerTypeName(customer.getCustomerType()));
        // 设置状态名称
        customerVO.setStatusName(customer.getStatus() == 1 ? "启用" : "禁用");
        return customerVO;
    }

    @Override
    @Transactional
    public void add(CustomerDTO customerDTO) {
        // 检查客户名称是否重复
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getCustomerName, customerDTO.getCustomerName());
        queryWrapper.eq(Customer::getDelFlag, 0);
        if (customerMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("客户名称已存在");
        }

        // 生成客户编号
        String customerCode = generateCustomerCode();

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 构建客户实体
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setCustomerCode(customerCode);
        customer.setOwnerName(SecurityUtils.getCurrentUserName());
        customer.setDeptId(SecurityUtils.getCurrentUserDeptId());
        customer.setDeptName(SecurityUtils.getCurrentUserDeptName());
        customer.setStatus(1);
        customer.setCreateBy(currentUserName);
        customer.setUpdateBy(currentUserName);
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
        customer.setDelFlag(0);

        // 保存客户
        customerMapper.insert(customer);
    }

    @Override
    @Transactional
    public void update(CustomerDTO customerDTO) {
        // 检查客户是否存在
        Customer existingCustomer = customerMapper.selectById(customerDTO.getId());
        if (existingCustomer == null || existingCustomer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查客户名称是否重复
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getCustomerName, customerDTO.getCustomerName());
        queryWrapper.ne(Customer::getId, customerDTO.getId());
        queryWrapper.eq(Customer::getDelFlag, 0);
        if (customerMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("客户名称已存在");
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 更新客户信息
        BeanUtils.copyProperties(customerDTO, existingCustomer);
        existingCustomer.setOwnerName(SecurityUtils.getCurrentUserName());
        existingCustomer.setUpdateBy(currentUserName);
        existingCustomer.setUpdateTime(LocalDateTime.now());

        // 保存更新
        customerMapper.updateById(existingCustomer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 逻辑删除客户
        customer.setDelFlag(1);
        customer.setUpdateBy(SecurityUtils.getCurrentUserName());
        customer.setUpdateTime(LocalDateTime.now());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 更新状态
        customer.setStatus(status);
        customer.setUpdateBy(SecurityUtils.getCurrentUserName());
        customer.setUpdateTime(LocalDateTime.now());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional
    public void markLoss(Long id, String lossReason) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 更新客户为流失状态
        customer.setCustomerType(4); // 流失客户
        customer.setLossReason(lossReason);
        customer.setLossTime(LocalDateTime.now());
        customer.setUpdateBy(SecurityUtils.getCurrentUserName());
        customer.setUpdateTime(LocalDateTime.now());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional
    public void recover(Long id) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 更新客户为潜在客户状态
        customer.setCustomerType(1); // 潜在客户
        customer.setLossReason(null);
        customer.setLossTime(null);
        customer.setUpdateBy(SecurityUtils.getCurrentUserName());
        customer.setUpdateTime(LocalDateTime.now());
        customerMapper.updateById(customer);
    }

    @Override
    public void importExcel(MultipartFile file) {
        // TODO: 实现Excel导入功能
        throw new RuntimeException("Excel导入功能暂未实现");
    }

    @Override
    public void exportExcel(HttpServletResponse response, String customerName, String phone, Integer customerType, Long ownerId, Long deptId) {
        // TODO: 实现Excel导出功能
        throw new RuntimeException("Excel导出功能暂未实现");
    }

    @Override
    public List<CustomerVO> getCustomerSelect() {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getDelFlag, 0);
        queryWrapper.eq(Customer::getStatus, 1);
        queryWrapper.orderByDesc(Customer::getCreateTime);
        List<Customer> customers = customerMapper.selectList(queryWrapper);

        List<CustomerVO> customerVOs = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerVO customerVO = new CustomerVO();
            customerVO.setId(customer.getId());
            customerVO.setCustomerName(customer.getCustomerName());
            customerVOs.add(customerVO);
        }
        return customerVOs;
    }

    @Override
    public List<CustomerVO> getCustomerStats() {
        // TODO: 实现客户统计功能
        return new ArrayList<>();
    }

    /**
     * 生成客户编号
     */
    private String generateCustomerCode() {
        String prefix = "CUST" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Customer::getCustomerCode, prefix);
        queryWrapper.orderByDesc(Customer::getCustomerCode);
        Customer lastCustomer = customerMapper.selectOne(queryWrapper);
        if (lastCustomer == null) {
            return prefix + "001";
        } else {
            String lastCode = lastCustomer.getCustomerCode();
            int sequence = Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1;
            return prefix + String.format("%03d", sequence);
        }
    }

    /**
     * 获取客户类型名称
     */
    private String getCustomerTypeName(Integer customerType) {
        switch (customerType) {
            case 1:
                return "潜在客户";
            case 2:
                return "意向客户";
            case 3:
                return "合作客户";
            case 4:
                return "流失客户";
            default:
                return "未知";
        }
    }
}
