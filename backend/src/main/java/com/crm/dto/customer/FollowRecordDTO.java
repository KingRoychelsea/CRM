package com.crm.dto.customer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 客户跟进记录DTO
 */
@Data
public class FollowRecordDTO {

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
     * 联系人ID
     */
    private Long contactId;

    /**
     * 跟进内容
     */
    @NotBlank(message = "跟进内容不能为空")
    private String content;

    /**
     * 跟进方式（1-电话 2-微信 3-面谈 4-邮件 5-其他）
     */
    @NotNull(message = "跟进方式不能为空")
    private Integer followType;

    /**
     * 跟进时间
     */
    @NotNull(message = "跟进时间不能为空")
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
     * 备注
     */
    private String remark;
}
