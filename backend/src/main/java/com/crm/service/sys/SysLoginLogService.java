package com.crm.service.sys;

import com.crm.entity.sys.SysLoginLog;
import com.crm.vo.sys.SysLoginLogVO;

import java.util.List;

/**
 * 登录日志服务接口
 */
public interface SysLoginLogService {

    /**
     * 登录日志分页查询
     */
    List<SysLoginLogVO> list(Integer pageNum, Integer pageSize, String loginUser, Integer status, String loginIp, String startTime, String endTime);

    /**
     * 获取登录日志详情
     */
    SysLoginLogVO getById(Long id);

    /**
     * 删除登录日志
     */
    boolean delete(Long id);

    /**
     * 批量删除登录日志
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 清空登录日志
     */
    boolean clear();

    /**
     * 保存登录日志
     */
    boolean saveLog(SysLoginLog sysLoginLog);
}
