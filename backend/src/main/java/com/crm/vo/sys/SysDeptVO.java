package com.crm.vo.sys;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门VO
 */
@Data
public class SysDeptVO {

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
     * 父部门名称
     */
    private String parentName;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 子部门
     */
    private List<SysDeptVO> children;

    /**
     * 是否有子部门
     */
    private boolean hasChildren;
}
