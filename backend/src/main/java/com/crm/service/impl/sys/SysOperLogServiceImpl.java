package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysOperLog;
import com.crm.mapper.sys.SysOperLogMapper;
import com.crm.service.sys.SysOperLogService;
import com.crm.vo.sys.SysOperLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志服务实现
 */
@Slf4j
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    public List<SysOperLogVO> list(Integer pageNum, Integer pageSize, String operUser, String operType, String operModule, String startTime, String endTime) {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        if (operUser != null) {
            wrapper.like(SysOperLog::getOperName, operUser);
        }
        if (operType != null) {
            wrapper.like(SysOperLog::getTitle, operType);
        }
        if (operModule != null) {
            wrapper.like(SysOperLog::getOperUrl, operModule);
        }
        if (startTime != null) {
            wrapper.ge(SysOperLog::getOperTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(SysOperLog::getOperTime, endTime);
        }
        wrapper.orderByDesc(SysOperLog::getOperTime);

        List<SysOperLog> operLogs = baseMapper.selectList(wrapper);
        List<SysOperLogVO> voList = new ArrayList<>();

        for (SysOperLog operLog : operLogs) {
            SysOperLogVO vo = new SysOperLogVO();
            BeanUtils.copyProperties(operLog, vo);
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public SysOperLogVO getById(Long id) {
        SysOperLog operLog = baseMapper.selectById(id);
        if (operLog != null) {
            SysOperLogVO vo = new SysOperLogVO();
            BeanUtils.copyProperties(operLog, vo);
            return vo;
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean clear() {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        return remove(wrapper);
    }

    @Override
    public boolean saveLog(SysOperLog sysOperLog) {
        return save(sysOperLog);
    }
}
