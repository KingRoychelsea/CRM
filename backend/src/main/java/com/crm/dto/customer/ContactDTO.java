package com.crm.dto.customer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 联系人信息DTO
 */
@Data
public class ContactDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 性别（0-女 1-男）
     */
    private Integer gender;

    /**
     * 职位
     */
    private String position;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 优先级（1-高 2-中 3-低）
     */
    private Integer priority;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;
}
