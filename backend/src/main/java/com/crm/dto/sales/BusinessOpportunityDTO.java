package com.crm.dto.sales;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商机信息DTO
 */
@Data
public class BusinessOpportunityDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商机名称
     */
    @NotBlank(message = "商机名称不能为空")
    private String opportunityName;

    /**
     * 客户ID
     */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 预计金额
     */
    @NotNull(message = "预计金额不能为空")
    private BigDecimal expectedAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 商机阶段（1-初步接洽 2-需求确认 3-方案报价 4-合同签订 5-成功 6-失败）
     */
    @NotNull(message = "商机阶段不能为空")
    private Integer stage;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人不能为空")
    private Long ownerId;

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
     * 失败原因
     */
    private String failReason;

    /**
     * 备注
     */
    private String remark;
}
