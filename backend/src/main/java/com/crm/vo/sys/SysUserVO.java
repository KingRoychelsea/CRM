package com.crm.vo.sys;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户VO
 */
@Data
public class SysUserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 角色名称列表
     */
    private List<String> roleNames;
}
