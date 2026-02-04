package com.crm.dto.sales;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同信息DTO
 */
@Data
public class ContractDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不能为空")
    private String contractName;

    /**
     * 客户ID
     */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /**
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 合同金额
     */
    @NotNull(message = "合同金额不能为空")
    private BigDecimal contractAmount;

    /**
     * 已回款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 合同开始时间
     */
    @NotNull(message = "合同开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    @NotNull(message = "合同结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 合同状态（1-草稿 2-生效 3-终止 4-过期）
     */
    @NotNull(message = "合同状态不能为空")
    private Integer status;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人不能为空")
    private Long ownerId;

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
}
