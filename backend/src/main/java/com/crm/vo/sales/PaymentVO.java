package com.crm.vo.sales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 回款信息VO
 */
@Data
public class PaymentVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 回款名称
     */
    private String paymentName;

    /**
     * 回款编号
     */
    private String paymentCode;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 合同ID
     */
    private Long contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 回款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 回款时间
     */
    private LocalDateTime paymentTime;

    /**
     * 回款方式（1-银行转账 2-现金 3-支付宝 4-微信 5-其他）
     */
    private Integer paymentMethod;

    /**
     * 回款方式名称
     */
    private String paymentMethodName;

    /**
     * 回款状态（1-待回款 2-已回款 3-部分回款）
     */
    private Integer status;

    /**
     * 回款状态名称
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
