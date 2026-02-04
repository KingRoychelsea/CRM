package com.crm.dto.sales;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息DTO
 */
@Data
public class OrderDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单名称
     */
    @NotBlank(message = "订单名称不能为空")
    private String orderName;

    /**
     * 客户ID
     */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /**
     * 合同ID
     */
    private Long contractId;

    /**
     * 订单金额
     */
    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmount;

    /**
     * 已回款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 订单状态（1-待付款 2-已付款 3-待发货 4-已完成 5-已取消）
     */
    @NotNull(message = "订单状态不能为空")
    private Integer status;

    /**
     * 负责人ID
     */
    @NotNull(message = "负责人不能为空")
    private Long ownerId;

    /**
     * 订单日期
     */
    @NotNull(message = "订单日期不能为空")
    private LocalDateTime orderDate;

    /**
     * 付款日期
     */
    private LocalDateTime payDate;

    /**
     * 完成日期
     */
    private LocalDateTime completeDate;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 备注
     */
    private String remark;
}
