package com.crm.dto.sales;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 回款信息DTO
 */
@Data
public class PaymentDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 回款名称
     */
    @NotBlank(message = "回款名称不能为空")
    private String paymentName;

    /**
     * 客户ID
     */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 合同ID
     */
    private Long contractId;

    /**
     * 回款金额
     */
    @NotNull(message = "回款金额不能为空")
    private BigDecimal paymentAmount;

    /**
     * 回款时间
     */
    @NotNull(message = "回款时间不能为空")
    private LocalDateTime paymentTime;

    /**
     * 回款方式（1-银行转账 2-现金 3-支付宝 4-微信 5-其他）
     */
    @NotNull(message = "回款方式不能为空")
    private Integer paymentMethod;

    /**
     * 回款状态（1-待回款 2-已回款 3-部分回款）
     */
    @NotNull(message = "回款状态不能为空")
    private Integer status;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人不能为空")
    private Long ownerId;

    /**
     * 备注
     */
    private String remark;
}
