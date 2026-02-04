package com.crm.service.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.ContactDTO;
import com.crm.entity.customer.Contact;
import com.crm.entity.customer.Customer;
import com.crm.mapper.customer.ContactMapper;
import com.crm.mapper.customer.CustomerMapper;
import com.crm.service.customer.ContactService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.customer.ContactVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人信息服务实现类
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Page<ContactVO> list(Integer pageNum, Integer pageSize, Long customerId, String contactName, String phone, Integer priority) {
        // 构建查询条件
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        if (customerId != null) {
            queryWrapper.eq(Contact::getCustomerId, customerId);
        }
        if (StringUtils.isNotBlank(contactName)) {
            queryWrapper.like(Contact::getContactName, contactName);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like(Contact::getPhone, phone);
        }
        if (priority != null) {
            queryWrapper.eq(Contact::getPriority, priority);
        }
        queryWrapper.eq(Contact::getDelFlag, 0);
        queryWrapper.orderByDesc(Contact::getCreateTime);

        // 分页查询
        Page<Contact> page = new Page<>(pageNum, pageSize);
        contactMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<ContactVO> pageVO = new Page<>();
        BeanUtils.copyProperties(page, pageVO);
        List<ContactVO> records = new ArrayList<>();
        for (Contact contact : page.getRecords()) {
            ContactVO contactVO = new ContactVO();
            BeanUtils.copyProperties(contact, contactVO);
            // 设置性别名称
            contactVO.setGenderName(contact.getGender() == 1 ? "男" : "女");
            // 设置优先级名称
            contactVO.setPriorityName(getPriorityName(contact.getPriority()));
            // 设置状态名称
            contactVO.setStatusName(contact.getStatus() == 1 ? "启用" : "禁用");
            records.add(contactVO);
        }
        pageVO.setRecords(records);
        return pageVO;
    }

    @Override
    public ContactVO getById(Long id) {
        Contact contact = contactMapper.selectById(id);
        if (contact == null || contact.getDelFlag() == 1) {
            throw new RuntimeException("联系人不存在");
        }
        ContactVO contactVO = new ContactVO();
        BeanUtils.copyProperties(contact, contactVO);
        // 设置性别名称
        contactVO.setGenderName(contact.getGender() == 1 ? "男" : "女");
        // 设置优先级名称
        contactVO.setPriorityName(getPriorityName(contact.getPriority()));
        // 设置状态名称
        contactVO.setStatusName(contact.getStatus() == 1 ? "启用" : "禁用");
        return contactVO;
    }

    @Override
    @Transactional
    public void add(ContactDTO contactDTO) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(contactDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人名称是否重复（同一客户下）
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contact::getCustomerId, contactDTO.getCustomerId());
        queryWrapper.eq(Contact::getContactName, contactDTO.getContactName());
        queryWrapper.eq(Contact::getDelFlag, 0);
        if (contactMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("该客户下联系人名称已存在");
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 构建联系人实体
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactDTO, contact);
        contact.setCustomerName(customer.getCustomerName());
        contact.setStatus(1);
        contact.setCreateBy(currentUserName);
        contact.setUpdateBy(currentUserName);
        contact.setCreateTime(LocalDateTime.now());
        contact.setUpdateTime(LocalDateTime.now());
        contact.setDelFlag(0);

        // 保存联系人
        contactMapper.insert(contact);
    }

    @Override
    @Transactional
    public void update(ContactDTO contactDTO) {
        // 检查联系人是否存在
        Contact existingContact = contactMapper.selectById(contactDTO.getId());
        if (existingContact == null || existingContact.getDelFlag() == 1) {
            throw new RuntimeException("联系人不存在");
        }

        // 检查客户是否存在
        Customer customer = customerMapper.selectById(contactDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人名称是否重复（同一客户下）
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contact::getCustomerId, contactDTO.getCustomerId());
        queryWrapper.eq(Contact::getContactName, contactDTO.getContactName());
        queryWrapper.ne(Contact::getId, contactDTO.getId());
        queryWrapper.eq(Contact::getDelFlag, 0);
        if (contactMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("该客户下联系人名称已存在");
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 更新联系人信息
        BeanUtils.copyProperties(contactDTO, existingContact);
        existingContact.setCustomerName(customer.getCustomerName());
        existingContact.setUpdateBy(currentUserName);
        existingContact.setUpdateTime(LocalDateTime.now());

        // 保存更新
        contactMapper.updateById(existingContact);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查联系人是否存在
        Contact contact = contactMapper.selectById(id);
        if (contact == null || contact.getDelFlag() == 1) {
            throw new RuntimeException("联系人不存在");
        }

        // 逻辑删除联系人
        contact.setDelFlag(1);
        contact.setUpdateBy(SecurityUtils.getCurrentUserName());
        contact.setUpdateTime(LocalDateTime.now());
        contactMapper.updateById(contact);
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
        // 检查联系人是否存在
        Contact contact = contactMapper.selectById(id);
        if (contact == null || contact.getDelFlag() == 1) {
            throw new RuntimeException("联系人不存在");
        }

        // 更新状态
        contact.setStatus(status);
        contact.setUpdateBy(SecurityUtils.getCurrentUserName());
        contact.setUpdateTime(LocalDateTime.now());
        contactMapper.updateById(contact);
    }

    @Override
    public List<ContactVO> listByCustomerId(Long customerId) {
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contact::getCustomerId, customerId);
        queryWrapper.eq(Contact::getDelFlag, 0);
        queryWrapper.eq(Contact::getStatus, 1);
        queryWrapper.orderByDesc(Contact::getPriority);
        queryWrapper.orderByDesc(Contact::getCreateTime);
        List<Contact> contacts = contactMapper.selectList(queryWrapper);

        List<ContactVO> contactVOs = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactVO contactVO = new ContactVO();
            BeanUtils.copyProperties(contact, contactVO);
            // 设置性别名称
            contactVO.setGenderName(contact.getGender() == 1 ? "男" : "女");
            // 设置优先级名称
            contactVO.setPriorityName(getPriorityName(contact.getPriority()));
            // 设置状态名称
            contactVO.setStatusName(contact.getStatus() == 1 ? "启用" : "禁用");
            contactVOs.add(contactVO);
        }
        return contactVOs;
    }

    @Override
    public List<ContactVO> getContactSelect(Long customerId) {
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        if (customerId != null) {
            queryWrapper.eq(Contact::getCustomerId, customerId);
        }
        queryWrapper.eq(Contact::getDelFlag, 0);
        queryWrapper.eq(Contact::getStatus, 1);
        queryWrapper.orderByDesc(Contact::getPriority);
        queryWrapper.orderByDesc(Contact::getCreateTime);
        List<Contact> contacts = contactMapper.selectList(queryWrapper);

        List<ContactVO> contactVOs = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactVO contactVO = new ContactVO();
            contactVO.setId(contact.getId());
            contactVO.setContactName(contact.getContactName());
            contactVO.setPhone(contact.getPhone());
            contactVOs.add(contactVO);
        }
        return contactVOs;
    }

    /**
     * 获取优先级名称
     */
    private String getPriorityName(Integer priority) {
        switch (priority) {
            case 1:
                return "高";
            case 2:
                return "中";
            case 3:
                return "低";
            default:
                return "未知";
        }
    }
}
