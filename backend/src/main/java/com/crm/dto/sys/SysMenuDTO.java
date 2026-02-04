package com.crm.dto.sys;

import lombok.Data;

/**
 * 菜单DTO
 */
@Data
public class SysMenuDTO {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否为外部链接
     */
    private String isFrame;

    /**
     * 是否缓存
     */
    private String isCache;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单状态
     */
    private String visible;

    /**
     * 菜单状态
     */
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;
}
