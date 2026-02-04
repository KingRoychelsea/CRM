package com.crm.dto.sys;

import lombok.Data;

import java.util.List;

/**
 * 角色DTO
 */
@Data
public class SysRoleDTO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色关键字
     */
    private String roleKey;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
