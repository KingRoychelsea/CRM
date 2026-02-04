package com.crm.service.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.customer.FollowRecordDTO;
import com.crm.entity.customer.Contact;
import com.crm.entity.customer.Customer;
import com.crm.entity.customer.FollowRecord;
import com.crm.mapper.customer.ContactMapper;
import com.crm.mapper.customer.CustomerMapper;
import com.crm.mapper.customer.FollowRecordMapper;
import com.crm.service.customer.FollowRecordService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.customer.FollowRecordVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户跟进记录服务实现类
 */
@Service
public class FollowRecordServiceImpl implements FollowRecordService {

    @Autowired
    private FollowRecordMapper followRecordMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public Page<FollowRecordVO> list(Integer pageNum, Integer pageSize, Long customerId, Long contactId, Integer followType, Long followUserId, String startTime, String endTime) {
        // 构建查询条件
        LambdaQueryWrapper<FollowRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (customerId != null) {
            queryWrapper.eq(FollowRecord::getCustomerId, customerId);
        }
        if (contactId != null) {
            queryWrapper.eq(FollowRecord::getContactId, contactId);
        }
        if (followType != null) {
            queryWrapper.eq(FollowRecord::getFollowType, followType);
        }
        if (followUserId != null) {
            queryWrapper.eq(FollowRecord::getFollowUserId, followUserId);
        }
        if (StringUtils.isNotBlank(startTime)) {
            queryWrapper.ge(FollowRecord::getFollowTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryWrapper.le(FollowRecord::getFollowTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        queryWrapper.eq(FollowRecord::getDelFlag, 0);
        queryWrapper.orderByDesc(FollowRecord::getFollowTime);

        // 分页查询
        Page<FollowRecord> page = new Page<>(pageNum, pageSize);
        followRecordMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<FollowRecordVO> pageVO = new Page<>();
        BeanUtils.copyProperties(page, pageVO);
        List<FollowRecordVO> records = new ArrayList<>();
        for (FollowRecord followRecord : page.getRecords()) {
            FollowRecordVO followRecordVO = new FollowRecordVO();
            BeanUtils.copyProperties(followRecord, followRecordVO);
            // 设置跟进方式名称
            followRecordVO.setFollowTypeName(getFollowTypeName(followRecord.getFollowType()));
            // 设置跟进结果名称
            followRecordVO.setResultName(getResultName(followRecord.getResult()));
            records.add(followRecordVO);
        }
        pageVO.setRecords(records);
        return pageVO;
    }

    @Override
    public FollowRecordVO getById(Long id) {
        FollowRecord followRecord = followRecordMapper.selectById(id);
        if (followRecord == null || followRecord.getDelFlag() == 1) {
            throw new RuntimeException("跟进记录不存在");
        }
        FollowRecordVO followRecordVO = new FollowRecordVO();
        BeanUtils.copyProperties(followRecord, followRecordVO);
        // 设置跟进方式名称
        followRecordVO.setFollowTypeName(getFollowTypeName(followRecord.getFollowType()));
        // 设置跟进结果名称
        followRecordVO.setResultName(getResultName(followRecord.getResult()));
        return followRecordVO;
    }

    @Override
    @Transactional
    public void add(FollowRecordDTO followRecordDTO) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(followRecordDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人是否存在（如果指定了联系人）
        String contactName = null;
        if (followRecordDTO.getContactId() != null) {
            Contact contact = contactMapper.selectById(followRecordDTO.getContactId());
            if (contact == null || contact.getDelFlag() == 1) {
                throw new RuntimeException("联系人不存在");
            }
            contactName = contact.getContactName();
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // 构建跟进记录实体
        FollowRecord followRecord = new FollowRecord();
        BeanUtils.copyProperties(followRecordDTO, followRecord);
        followRecord.setCustomerName(customer.getCustomerName());
        followRecord.setContactName(contactName);
        followRecord.setFollowUserId(currentUserId);
        followRecord.setFollowUserName(currentUserName);
        followRecord.setCreateBy(currentUserName);
        followRecord.setUpdateBy(currentUserName);
        followRecord.setCreateTime(LocalDateTime.now());
        followRecord.setUpdateTime(LocalDateTime.now());
        followRecord.setDelFlag(0);

        // 保存跟进记录
        followRecordMapper.insert(followRecord);
    }

    @Override
    @Transactional
    public void update(FollowRecordDTO followRecordDTO) {
        // 检查跟进记录是否存在
        FollowRecord existingFollowRecord = followRecordMapper.selectById(followRecordDTO.getId());
        if (existingFollowRecord == null || existingFollowRecord.getDelFlag() == 1) {
            throw new RuntimeException("跟进记录不存在");
        }

        // 检查客户是否存在
        Customer customer = customerMapper.selectById(followRecordDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人是否存在（如果指定了联系人）
        String contactName = null;
        if (followRecordDTO.getContactId() != null) {
            Contact contact = contactMapper.selectById(followRecordDTO.getContactId());
            if (contact == null || contact.getDelFlag() == 1) {
                throw new RuntimeException("联系人不存在");
            }
            contactName = contact.getContactName();
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 更新跟进记录信息
        BeanUtils.copyProperties(followRecordDTO, existingFollowRecord);
        existingFollowRecord.setCustomerName(customer.getCustomerName());
        existingFollowRecord.setContactName(contactName);
        existingFollowRecord.setUpdateBy(currentUserName);
        existingFollowRecord.setUpdateTime(LocalDateTime.now());

        // 保存更新
        followRecordMapper.updateById(existingFollowRecord);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查跟进记录是否存在
        FollowRecord followRecord = followRecordMapper.selectById(id);
        if (followRecord == null || followRecord.getDelFlag() == 1) {
            throw new RuntimeException("跟进记录不存在");
        }

        // 逻辑删除跟进记录
        followRecord.setDelFlag(1);
        followRecord.setUpdateBy(SecurityUtils.getCurrentUserName());
        followRecord.setUpdateTime(LocalDateTime.now());
        followRecordMapper.updateById(followRecord);
    }

    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    public List<FollowRecordVO> listByCustomerId(Long customerId) {
        LambdaQueryWrapper<FollowRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FollowRecord::getCustomerId, customerId);
        queryWrapper.eq(FollowRecord::getDelFlag, 0);
        queryWrapper.orderByDesc(FollowRecord::getFollowTime);
        List<FollowRecord> followRecords = followRecordMapper.selectList(queryWrapper);

        List<FollowRecordVO> followRecordVOs = new ArrayList<>();
        for (FollowRecord followRecord : followRecords) {
            FollowRecordVO followRecordVO = new FollowRecordVO();
            BeanUtils.copyProperties(followRecord, followRecordVO);
            // 设置跟进方式名称
            followRecordVO.setFollowTypeName(getFollowTypeName(followRecord.getFollowType()));
            // 设置跟进结果名称
            followRecordVO.setResultName(getResultName(followRecord.getResult()));
            followRecordVOs.add(followRecordVO);
        }
        return followRecordVOs;
    }

    @Override
    public List<FollowRecordVO> getFollowRemind() {
        // 获取当前用户的待跟进记录（下次跟进时间在今天或之前）
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<FollowRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FollowRecord::getFollowUserId, SecurityUtils.getCurrentUserId());
        queryWrapper.le(FollowRecord::getNextFollowTime, now);
        queryWrapper.eq(FollowRecord::getDelFlag, 0);
        queryWrapper.orderByAsc(FollowRecord::getNextFollowTime);
        List<FollowRecord> followRecords = followRecordMapper.selectList(queryWrapper);

        List<FollowRecordVO> followRecordVOs = new ArrayList<>();
        for (FollowRecord followRecord : followRecords) {
            FollowRecordVO followRecordVO = new FollowRecordVO();
            BeanUtils.copyProperties(followRecord, followRecordVO);
            // 设置跟进方式名称
            followRecordVO.setFollowTypeName(getFollowTypeName(followRecord.getFollowType()));
            // 设置跟进结果名称
            followRecordVO.setResultName(getResultName(followRecord.getResult()));
            followRecordVOs.add(followRecordVO);
        }
        return followRecordVOs;
    }

    /**
     * 获取跟进方式名称
     */
    private String getFollowTypeName(Integer followType) {
        switch (followType) {
            case 1:
                return "电话";
            case 2:
                return "微信";
            case 3:
                return "面谈";
            case 4:
                return "邮件";
            case 5:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取跟进结果名称
     */
    private String getResultName(Integer result) {
        switch (result) {
            case 1:
                return "成功";
            case 2:
                return "失败";
            case 3:
                return "进行中";
            default:
                return "未知";
        }
    }
}
