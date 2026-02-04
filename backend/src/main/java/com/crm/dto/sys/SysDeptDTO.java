package com.crm.dto.sys;

import lombok.Data;

/**
 * 部门DTO
 */
@Data
public class SysDeptDTO {

    /**
     * 部门ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
