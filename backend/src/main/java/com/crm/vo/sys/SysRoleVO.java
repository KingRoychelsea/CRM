package com.crm.vo.sys;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色VO
 */
@Data
public class SysRoleVO {

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户数量
     */
    private Integer userCount;

    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;
}
