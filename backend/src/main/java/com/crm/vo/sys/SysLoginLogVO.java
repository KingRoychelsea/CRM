package com.crm.vo.sys;

import lombok.Data;

import java.util.Date;

/**
 * 登录日志视图对象
 */
@Data
public class SysLoginLogVO {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录状态（0-失败，1-成功）
     */
    private String status;

    /**
     * IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 登录时间
     */
    private Date loginTime;
}
