package com.crm.entity.sales;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商机信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("business_opportunity")
public class BusinessOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 逻辑删除标记（0-未删除 1-已删除）
     */
    @TableLogic
    private Integer delFlag;
}
