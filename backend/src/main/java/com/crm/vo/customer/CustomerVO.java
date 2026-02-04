package com.crm.vo.customer;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户信息VO
 */
@Data
public class CustomerVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 客户类型（1-潜在客户 2-意向客户 3-合作客户 4-流失客户）
     */
    private Integer customerType;

    /**
     * 客户类型名称
     */
    private String customerTypeName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 规模
     */
    private String scale;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

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
     * 客户来源
     */
    private String source;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 流失原因
     */
    private String lossReason;

    /**
     * 流失时间
     */
    private LocalDateTime lossTime;

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
