package com.crm.entity.sales;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 回款信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 回款状态（1-待回款 2-已回款 3-部分回款）
     */
    private Integer status;

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
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 计划回款日期
     */
    private LocalDateTime planDate;

    /**
     * 逻辑删除标记（0-未删除 1-已删除）
     */
    @TableLogic
    private Integer delFlag;
}
