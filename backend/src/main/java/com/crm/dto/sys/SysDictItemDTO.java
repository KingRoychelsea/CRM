package com.crm.dto.sys;

import lombok.Data;

/**
 * 字典项DTO
 */
@Data
public class SysDictItemDTO {

    /**
     * 字典项ID
     */
    private Long id;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典项名称
     */
    private String itemName;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 备注
     */
    private String remark;
}
