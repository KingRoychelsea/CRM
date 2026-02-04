package com.crm.vo.customer;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 联系人信息VO
 */
@Data
public class ContactVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 性别（0-女 1-男）
     */
    private Integer gender;

    /**
     * 性别名称
     */
    private String genderName;

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
     * 优先级名称
     */
    private String priorityName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0-禁用 1-启用）
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

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
