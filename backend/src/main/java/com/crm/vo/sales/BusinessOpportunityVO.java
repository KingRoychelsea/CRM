package com.crm.vo.sales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商机信息VO
 */
@Data
public class BusinessOpportunityVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商机名称
     */
    private String opportunityName;

    /**
     * 商机编号
     */
    private String opportunityCode;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 预计金额
     */
    private BigDecimal expectedAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 商机阶段（1-初步接洽 2-需求确认 3-方案报价 4-合同签订 5-成功 6-失败）
     */
    private Integer stage;

    /**
     * 商机阶段名称
     */
    private String stageName;

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
     * 预计成交时间
     */
    private LocalDateTime expectedCloseTime;

    /**
     * 实际成交时间
     */
    private LocalDateTime actualCloseTime;

    /**
     * 商机来源
     */
    private String source;

    /**
     * 成功率（0-100）
     */
    private Integer successRate;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 失败原因
     */
    private String failReason;

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
