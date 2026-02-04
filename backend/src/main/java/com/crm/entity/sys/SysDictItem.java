package com.crm.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典详情实体类
 */
@Data
@TableName("sys_dict_item")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典项ID
     */
    @TableId
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
     * 状态（0-禁用，1-启用）
     */
    private String status;

    /**
     * 排序号
     */
    private Integer orderNum;

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
}
