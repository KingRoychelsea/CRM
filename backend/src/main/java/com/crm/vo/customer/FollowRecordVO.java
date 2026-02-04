package com.crm.vo.customer;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户跟进记录VO
 */
@Data
public class FollowRecordVO {

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
     * 联系人ID
     */
    private Long contactId;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 跟进方式（1-电话 2-微信 3-面谈 4-邮件 5-其他）
     */
    private Integer followType;

    /**
     * 跟进方式名称
     */
    private String followTypeName;

    /**
     * 跟进人ID
     */
    private Long followUserId;

    /**
     * 跟进人姓名
     */
    private String followUserName;

    /**
     * 跟进时间
     */
    private LocalDateTime followTime;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 跟进结果（1-成功 2-失败 3-进行中）
     */
    private Integer result;

    /**
     * 跟进结果名称
     */
    private String resultName;

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
