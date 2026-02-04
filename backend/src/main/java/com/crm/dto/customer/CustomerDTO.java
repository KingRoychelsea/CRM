package com.crm.dto.customer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 客户信息DTO
 */
@Data
public class CustomerDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /**
     * 客户编号
     */
    private String customerCode;

    /**
     * 客户类型（1-潜在客户 2-意向客户 3-合作客户 4-流失客户）
     */
    @NotNull(message = "客户类型不能为空")
    private Integer customerType;

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
    @NotNull(message = "负责人不能为空")
    private Long ownerId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 客户来源
     */
    private String source;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;

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
}
