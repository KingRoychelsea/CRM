package com.crm.vo.sales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同信息VO
 */
@Data
public class ContractVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 商机名称
     */
    private String opportunityName;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 已回款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 合同开始时间
     */
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    private LocalDateTime endTime;

    /**
     * 合同状态（1-草稿 2-生效 3-终止 4-过期）
     */
    private Integer status;

    /**
     * 合同状态名称
     */
    private String statusName;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 负责人姓名
     */
    private String ownerName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同附件
     */
    private String attachment;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
