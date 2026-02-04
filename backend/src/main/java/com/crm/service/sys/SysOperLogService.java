package com.crm.service.sys;

import com.crm.entity.sys.SysOperLog;
import com.crm.vo.sys.SysOperLogVO;

import java.util.List;

/**
 * 操作日志服务接口
 */
public interface SysOperLogService {

    /**
     * 操作日志分页查询
     */
    List<SysOperLogVO> list(Integer pageNum, Integer pageSize, String operUser, String operType, String operModule, String startTime, String endTime);

    /**
     * 获取操作日志详情
     */
    SysOperLogVO getById(Long id);

    /**
     * 删除操作日志
     */
    boolean delete(Long id);

    /**
     * 批量删除操作日志
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 清空操作日志
     */
    boolean clear();

    /**
     * 保存操作日志
     */
    boolean saveLog(SysOperLog sysOperLog);
}
