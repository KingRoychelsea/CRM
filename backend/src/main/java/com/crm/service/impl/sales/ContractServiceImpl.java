package com.crm.service.impl.sales;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.dto.sales.ContractDTO;
import com.crm.entity.customer.Customer;
import com.crm.entity.sales.BusinessOpportunity;
import com.crm.entity.sales.Contract;
import com.crm.mapper.customer.CustomerMapper;
import com.crm.mapper.sales.BusinessOpportunityMapper;
import com.crm.mapper.sales.ContractMapper;
import com.crm.service.sales.ContractService;
import com.crm.utils.SecurityUtils;
import com.crm.vo.sales.ContractVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同信息服务实现类
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Override
    public Page<ContractVO> list(Integer pageNum, Integer pageSize, String contractName, Long customerId, Long opportunityId, Integer status, Long ownerId, Long deptId, String startTime, String endTime) {
        // 构建查询条件
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(contractName)) {
            queryWrapper.like(Contract::getContractName, contractName);
        }
        if (customerId != null) {
            queryWrapper.eq(Contract::getCustomerId, customerId);
        }
        if (opportunityId != null) {
            queryWrapper.eq(Contract::getOpportunityId, opportunityId);
        }
        if (status != null) {
            queryWrapper.eq(Contract::getStatus, status);
        }
        if (ownerId != null) {
            queryWrapper.eq(Contract::getOwnerId, ownerId);
        }
        if (deptId != null) {
            queryWrapper.eq(Contract::getDeptId, deptId);
        }
        if (StringUtils.isNotBlank(startTime)) {
            queryWrapper.ge(Contract::getCreateTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryWrapper.le(Contract::getCreateTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        queryWrapper.eq(Contract::getDelFlag, 0);
        queryWrapper.orderByDesc(Contract::getCreateTime);

        // 分页查询
        Page<Contract> page = new Page<>(pageNum, pageSize);
        contractMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<ContractVO> pageVO = new Page<>();
        BeanUtils.copyProperties(page, pageVO);
        List<ContractVO> records = new ArrayList<>();
        for (Contract contract : page.getRecords()) {
            ContractVO contractVO = new ContractVO();
            BeanUtils.copyProperties(contract, contractVO);
            // 设置状态名称
            contractVO.setStatusName(getStatusName(contract.getStatus()));
            records.add(contractVO);
        }
        pageVO.setRecords(records);
        return pageVO;
    }

    @Override
    public ContractVO getById(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null || contract.getDelFlag() == 1) {
            throw new RuntimeException("合同不存在");
        }
        ContractVO contractVO = new ContractVO();
        BeanUtils.copyProperties(contract, contractVO);
        // 设置状态名称
        contractVO.setStatusName(getStatusName(contract.getStatus()));
        return contractVO;
    }

    @Override
    @Transactional
    public void add(ContractDTO contractDTO) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(contractDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查商机是否存在（如果指定了商机）
        String opportunityName = null;
        if (contractDTO.getOpportunityId() != null) {
            BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(contractDTO.getOpportunityId());
            if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
                throw new RuntimeException("商机不存在");
            }
            opportunityName = businessOpportunity.getOpportunityName();
        }

        // 生成合同编号
        String contractCode = generateContractCode();

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Long currentUserDeptId = SecurityUtils.getCurrentUserDeptId();
        String currentUserDeptName = SecurityUtils.getCurrentUserDeptName();

        // 构建合同实体
        Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO, contract);
        contract.setContractCode(contractCode);
        contract.setCustomerName(customer.getCustomerName());
        contract.setOpportunityName(opportunityName);
        contract.setOwnerId(currentUserId);
        contract.setOwnerName(currentUserName);
        contract.setDeptId(currentUserDeptId);
        contract.setDeptName(currentUserDeptName);
        contract.setReceivedAmount(contractDTO.getContractAmount().subtract(contractDTO.getReceivedAmount()));
        contract.setCreateBy(currentUserName);
        contract.setUpdateBy(currentUserName);
        contract.setCreateTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());
        contract.setDelFlag(0);

        // 保存合同
        contractMapper.insert(contract);
    }

    @Override
    @Transactional
    public void update(ContractDTO contractDTO) {
        // 检查合同是否存在
        Contract existingContract = contractMapper.selectById(contractDTO.getId());
        if (existingContract == null || existingContract.getDelFlag() == 1) {
            throw new RuntimeException("合同不存在");
        }

        // 检查客户是否存在
        Customer customer = customerMapper.selectById(contractDTO.getCustomerId());
        if (customer == null || customer.getDelFlag() == 1) {
            throw new RuntimeException("客户不存在");
        }

        // 检查商机是否存在（如果指定了商机）
        String opportunityName = null;
        if (contractDTO.getOpportunityId() != null) {
            BusinessOpportunity businessOpportunity = businessOpportunityMapper.selectById(contractDTO.getOpportunityId());
            if (businessOpportunity == null || businessOpportunity.getDelFlag() == 1) {
                throw new RuntimeException("商机不存在");
            }
            opportunityName = businessOpportunity.getOpportunityName();
        }

        // 获取当前用户信息
        String currentUserName = SecurityUtils.getCurrentUserName();

        // 更新合同信息
        BeanUtils.copyProperties(contractDTO, existingContract);
        existingContract.setCustomerName(customer.getCustomerName());
        existingContract.setOpportunityName(opportunityName);
        existingContract.setUpdateBy(currentUserName);
        existingContract.setUpdateTime(LocalDateTime.now());

        // 保存更新
        contractMapper.updateById(existingContract);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查合同是否存在
        Contract contract = contractMapper.selectById(id);
        if (contract == null || contract.getDelFlag() == 1) {
            throw new RuntimeException("合同不存在");
        }

        // 逻辑删除合同
        contract.setDelFlag(1);
        contract.setUpdateBy(SecurityUtils.getCurrentUserName());
        contract.setUpdateTime(LocalDateTime.now());
        contractMapper.updateById(contract);
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
        // 检查合同是否存在
        Contract contract = contractMapper.selectById(id);
        if (contract == null || contract.getDelFlag() == 1) {
            throw new RuntimeException("合同不存在");
        }

        // 更新状态
        contract.setStatus(status);
        contract.setUpdateBy(SecurityUtils.getCurrentUserName());
        contract.setUpdateTime(LocalDateTime.now());
        contractMapper.updateById(contract);
    }

    @Override
    public String uploadAttachment(Long contractId, MultipartFile file) {
        // TODO: 实现附件上传功能
        throw new RuntimeException("附件上传功能暂未实现");
    }

    @Override
    public void downloadAttachment(Long id, HttpServletResponse response) {
        // TODO: 实现附件下载功能
        throw new RuntimeException("附件下载功能暂未实现");
    }

    @Override
    public void exportExcel(HttpServletResponse response, String contractName, Long customerId, Integer status, Long ownerId, Long deptId, String startTime, String endTime) {
        // TODO: 实现Excel导出功能
        throw new RuntimeException("Excel导出功能暂未实现");
    }

    @Override
    public List<ContractVO> getContractSelect() {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getDelFlag, 0);
        queryWrapper.orderByDesc(Contract::getCreateTime);
        List<Contract> contracts = contractMapper.selectList(queryWrapper);

        List<ContractVO> contractVOs = new ArrayList<>();
        for (Contract contract : contracts) {
            ContractVO contractVO = new ContractVO();
            contractVO.setId(contract.getId());
            contractVO.setContractName(contract.getContractName());
            contractVOs.add(contractVO);
        }
        return contractVOs;
    }

    @Override
    public List<ContractVO> getContractStats() {
        // TODO: 实现合同统计功能
        return new ArrayList<>();
    }

    /**
     * 生成合同编号
     */
    private String generateContractCode() {
        String prefix = "CON" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(Contract::getContractCode, prefix);
        queryWrapper.orderByDesc(Contract::getContractCode);
        Contract lastContract = contractMapper.selectOne(queryWrapper);
        if (lastContract == null) {
            return prefix + "001";
        } else {
            String lastCode = lastContract.getContractCode();
            int sequence = Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1;
            return prefix + String.format("%03d", sequence);
        }
    }

    /**
     * 获取合同状态名称
     */
    private String getStatusName(Integer status) {
        switch (status) {
            case 1:
                return "草稿";
            case 2:
                return "生效";
            case 3:
                return "终止";
            case 4:
                return "过期";
            default:
                return "未知";
        }
    }
}
