package com.crm.vo.sys;

import com.crm.entity.sys.SysDictItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 字典VO
 */
@Data
public class SysDictVO {

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 字典项列表
     */
    private List<SysDictItem> dictItems;
}
