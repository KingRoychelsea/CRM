package com.crm.vo.sales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息VO
 */
@Data
public class OrderVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同ID
     */
    private Long contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 已回款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 订单状态（1-待付款 2-已付款 3-待发货 4-已完成 5-已取消）
     */
    private Integer status;

    /**
     * 订单状态名称
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
     * 订单日期
     */
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
