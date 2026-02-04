package com.crm.dto.sys;

import lombok.Data;

/**
 * 字典DTO
 */
@Data
public class SysDictDTO {

    /**
     * 字典ID
     */
    private Long id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

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
