package com.crm.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门实体类
 */
@Data
@TableName("sys_dept")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId
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
     * 状态（0-禁用，1-启用）
     */
    private String status;

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
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children;

    /**
     * 父部门名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 是否有子部门
     */
    @TableField(exist = false)
    private boolean hasChildren;
}
