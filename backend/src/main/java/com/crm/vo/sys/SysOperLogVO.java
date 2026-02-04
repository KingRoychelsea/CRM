package com.crm.vo.sys;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志视图对象
 */
@Data
public class SysOperLogVO {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作标题
     */
    private String title;

    /**
     * 业务类型（0-其他，1-新增，2-修改，3-删除）
     */
    private Integer businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类型（0-其他，1-后台用户，2-手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 操作IP
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operTime;
}
