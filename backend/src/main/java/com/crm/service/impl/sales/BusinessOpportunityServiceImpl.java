package com.crm.service.impl.sales;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.BusinessOpportunityDTO;
import com.crm.entity.customer.Contact;
import com.crm.entity.customer.Customer;
import com.crm.entity.sales.BusinessOpportunity;
import com.crm.mapper.customer.ContactMapper;
import com.crm.mapper.customer.CustomerMapper;
import com.crm.mapper.sales.BusinessOpportunityMapper;
import com.crm.service.sales.BusinessOpportunityService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.sales.BusinessOpportunityVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 商机信息服务实现类
 */
@Service
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public Page<BusinessOpportunityVO> list(Integer pageNum, Integer pageSize, String opportunityName, Long customerId, Integer stage, Long ownerId, Long deptId, String startTime, String endTime) {
        // 构建查询条件
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(opportunityName)) {
            queryWrapper.like(BusinessOpportunity::getOpportunityName, opportunityName);
        }
        if (customerId != null) {
            queryWrapper.eq(BusinessOpportunity::getCustomerId, customerId);
        }
        if (stage != null) {
            queryWrapper.eq(BusinessOpportunity::getStage, stage);
        }
        if (ownerId != null) {
            queryWrapper.eq(BusinessOpportunity::getOwnerId, ownerId);
        }
        if (deptId != null) {
            queryWrapper.eq(BusinessOpportunity::getDeptId, deptId);
        }
        if (StringUtils.isNotBlank(startTime)) {
            queryWrapper.ge(BusinessOpportunity::getCreateTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryWrapper.le(BusinessOpportunity::getCreateTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        queryWrapper.eq(BusinessOpportunity::getDelFlag, 0);
        queryWrapper.orderByDesc(BusinessOpportunity::getCreateTime);

        // 分页查询
        Page<BusinessOpportunity> page = new Page<>(pageNum, pageSize);
        businessOpportunityMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<BusinessOpportunityVO> pageVO = new Page<>();
        BeanUtils.copyProperties(page, pageVO);
        List<BusinessOpportunityVO> records = new ArrayList<>();
        for (BusinessOpportunity businessOpportunity : page.getRecords()) {
            BusinessOpportunityVO businessOpportunityVO = new BusinessOpportunityVO();
            BeanUtils.copyProperties(businessOpportunity, businessOpportunityVO);
            // 设置阶段名称
            businessOpportunityVO.setStageName(getStageName(businessOpportunity.getStage()));
            // 设置状态名称
            businessOpportunityVO.setStatusName(businessOpportunity.getStatus() == 1 ? "启用" : "禁用");
            records.add(businessOpportunityVO);
        }
        pageVO.setRecords(records);
        return pageVO;
    }

    @Override
    public BusinessOpportunityVO getById(Long id) {
        BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(id);
        if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
            throw new RuntimeException("商机不存在");
        }
        BusinessOpportunityVO businessOpportunityVO = new BusinessOpportunityVO();
        BeanUtils.copyProperties(businessOpportunity, businessOpportunityVO);
        // 设置阶段名称
        businessOpportunityVO.setStageName(getStageName(businessOpportunity.getStage()));
        // 设置状态名称
        businessOpportunityVO.setStatusName(businessOpportunity.getStatus() == 1 ? "启用" : "禁用");
        return businessOpportunityVO;
    }

    @Override
    @Transactional
    public void add(BusinessOpportunityDTO businessOpportunityDTO) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(businessOpportunityDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人是否存在（如果指定了联系人）
        String contactName = null;
        if (businessOpportunityDTO.getContactId() != null) {
            Contact contact = contactMapper.selectById(businessOpportunityDTO.getContactId());
            if (contact == null || contact.getDelFlag() == 1) {
                throw new RuntimeException("联系人不存在");
            }
            contactName = contact.getContactName();
        }

        // 生成商机编号
        String opportunityCode = generateOpportunityCode();

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Long currentUserDeptId = SecurityUtils.getCurrentUserDeptId();
        String currentUserDeptName = SecurityUtils.getCurrentUserDeptName();

        // 构建商机实体
        BusinessOpportunity businessOpportunity = new BusinessOpportunity();
        BeanUtils.copyProperties(businessOpportunityDTO, businessOpportunity);
        businessOpportunity.setOpportunityCode(opportunityCode);
        businessOpportunity.setCustomerName(customer.getCustomerName());
        businessOpportunity.setContactName(contactName);
        businessOpportunity.setOwnerId(currentUserId);
        businessOpportunity.setOwnerName(currentUserName);
        businessOpportunity.setDeptId(currentUserDeptId);
        businessOpportunity.setDeptName(currentUserDeptName);
        businessOpportunity.setStatus(1);
        businessOpportunity.setCreateBy(currentUserName);
        businessOpportunity.setUpdateBy(currentUserName);
        businessOpportunity.setCreateTime(LocalDateTime.now());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunity.setDelFlag(0);

        // 保存商机
        businessOpportunityMapper.insert(businessOpportunity);
    }

    @Override
    @Transactional
    public void update(BusinessOpportunityDTO businessOpportunityDTO) {
        // 检查商机是否存在
        BusinessOpportunity existingBusinessOpportunity = businessOpportunityMapper.selectById(businessOpportunityDTO.getId());
        if (existingBusinessOpportunity == null || existingBusinessOpportunity.getDelFlag() == 1) {
            throw new RuntimeException("商机不存在");
        }

        // 检查客户是否存在
        Customer customer = customerMapper.selectById(businessOpportunityDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查联系人是否存在（如果指定了联系人）
        String contactName = null;
        if (businessOpportunityDTO.getContactId() != null) {
            Contact contact = contactMapper.selectById(businessOpportunityDTO.getContactId());
            if (contact == null || contact.getDelFlag() == 1) {
                throw new RuntimeException("联系人不存在");
            }
            contactName = contact.getContactName();
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 更新商机信息
        BeanUtils.copyProperties(businessOpportunityDTO, existingBusinessOpportunity);
        existingBusinessOpportunity.setCustomerName(customer.getCustomerName());
        existingBusinessOpportunity.setContactName(contactName);
        existingBusinessOpportunity.setUpdateBy(currentUserName);
        existingBusinessOpportunity.setUpdateTime(LocalDateTime.now());

        // 保存更新
        businessOpportunityMapper.updateById(existingBusinessOpportunity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查商机是否存在
        BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(id);
        if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
            throw new RuntimeException("商机不存在");
        }

        // 逻辑删除商机
        businessOpportunity.setDelFlag(1);
        businessOpportunity.setUpdateBy(SecurityUtils.getCurrentUserName());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunityMapper.updateById(businessOpportunity);
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
        // 检查商机是否存在
        BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(id);
        if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
            throw new RuntimeException("商机不存在");
        }

        // 更新状态
        businessOpportunity.setStatus(status);
        businessOpportunity.setUpdateBy(SecurityUtils.getCurrentUserName());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunityMapper.updateById(businessOpportunity);
    }

    @Override
    @Transactional
    public void updateStage(Long id, Integer stage, String failReason) {
        // 检查商机是否存在
        BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(id);
        if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
            throw new RuntimeException("商机不存在");
        }

        // 更新阶段
        businessOpportunity.setStage(stage);
        if (stage == 6) { // 失败
            businessOpportunity.setFailReason(failReason);
        }
        if (stage == 5) { // 成功
            businessOpportunity.setActualCloseTime(LocalDateTime.now());
        }
        businessOpportunity.setUpdateBy(SecurityUtils.getCurrentUserName());
        businessOpportunity.setUpdateTime(LocalDateTime.now());
        businessOpportunityMapper.updateById(businessOpportunity);
    }

    @Override
    public List<BusinessOpportunityVO> getOpportunitySelect() {
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessOpportunity::getDelFlag, 0);
        queryWrapper.eq(BusinessOpportunity::getStatus, 1);
        queryWrapper.orderByDesc(BusinessOpportunity::getCreateTime);
        List<BusinessOpportunity> businessOpportunities = businessOpportunityMapper.selectList(queryWrapper);

        List<BusinessOpportunityVO> businessOpportunityVOs = new ArrayList<>();
        for (BusinessOpportunity businessOpportunity : businessOpportunities) {
            BusinessOpportunityVO businessOpportunityVO = new BusinessOpportunityVO();
            businessOpportunityVO.setId(businessOpportunity.getId());
            businessOpportunityVO.setOpportunityName(businessOpportunity.getOpportunityName());
            businessOpportunityVOs.add(businessOpportunityVO);
        }
        return businessOpportunityVOs;
    }

    @Override
    public List<BusinessOpportunityVO> getOpportunityStats() {
        // TODO: 实现商机统计功能
        return new ArrayList<>();
    }

    @Override
    public void exportExcel(HttpServletResponse response, String fileName, Long customerId, Integer stage, Long ownerId, Long deptId, String startTime, String endTime) {
        try {
            LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessOpportunity::getDelFlag, 0);
            if (customerId != null) {
                queryWrapper.eq(BusinessOpportunity::getCustomerId, customerId);
            }
            if (stage != null) {
                queryWrapper.eq(BusinessOpportunity::getStage, stage);
            }
            if (ownerId != null) {
                queryWrapper.eq(BusinessOpportunity::getOwnerId, ownerId);
            }
            if (startTime != null && !startTime.isEmpty()) {
                queryWrapper.ge(BusinessOpportunity::getCreateTime, LocalDateTime.parse(startTime));
            }
            if (endTime != null && !endTime.isEmpty()) {
                queryWrapper.le(BusinessOpportunity::getCreateTime, LocalDateTime.parse(endTime));
            }
            queryWrapper.orderByDesc(BusinessOpportunity::getCreateTime);

            List<BusinessOpportunity> list = businessOpportunityMapper.selectList(queryWrapper);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("商机列表");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"商机编号", "商机名称", "客户名称", "商机金额", "商机阶段", "成交概率", "负责人", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < list.size(); i++) {
                BusinessOpportunity item = list.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getOpportunityCode());
                row.createCell(1).setCellValue(item.getOpportunityName());
                row.createCell(2).setCellValue(item.getCustomerName());
                row.createCell(3).setCellValue(item.getExpectedAmount() != null ? item.getExpectedAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(getStageName(item.getStage()));
                row.createCell(5).setCellValue(item.getSuccessRate() != null ? item.getSuccessRate().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getCreateTime() != null ? item.getCreateTime().toString() : "");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    /**
     * 生成商机编号
     */
    private String generateOpportunityCode() {
        String prefix = "OPP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(BusinessOpportunity::getOpportunityCode, prefix);
        queryWrapper.orderByDesc(BusinessOpportunity::getOpportunityCode);
        BusinessOpportunity lastOpportunity = businessOpportunityMapper.selectOne(queryWrapper);
        if (lastOpportunity == null) {
            return prefix + "001";
        } else {
            String lastCode = lastOpportunity.getOpportunityCode();
            int sequence = Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1;
            return prefix + String.format("%03d", sequence);
        }
    }

    /**
     * 获取商机阶段名称
     */
    private String getStageName(Integer stage) {
        switch (stage) {
            case 1:
                return "初步接洽";
            case 2:
                return "需求确认";
            case 3:
                return "方案报价";
            case 4:
                return "合同签订";
            case 5:
                return "成功";
            case 6:
                return "失败";
            default:
                return "未知";
        }
    }
}
