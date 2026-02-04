package com.crm.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体类
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId
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
     * 是否为外部链接（0-否，1-是）
     */
    private String isFrame;

    /**
     * 是否缓存（0-否，1-是）
     */
    private String isCache;

    /**
     * 菜单类型（M-目录，C-菜单，F-按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0-禁用，1-启用）
     */
    private String visible;

    /**
     * 菜单状态（0-禁用，1-启用）
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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标记（0-未删除，1-已删除）
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    // 非数据库字段
    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 是否选中
     */
    @TableField(exist = false)
    private boolean isSelect;
}
