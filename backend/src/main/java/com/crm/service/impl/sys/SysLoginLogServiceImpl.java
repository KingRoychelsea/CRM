package com.crm.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.sys.SysLoginLog;
import com.crm.mapper.sys.SysLoginLogMapper;
import com.crm.service.sys.SysLoginLogService;
import com.crm.vo.sys.SysLoginLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录日志服务实现
 */
@Slf4j
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public List<SysLoginLogVO> list(Integer pageNum, Integer pageSize, String loginUser, Integer status, String loginIp, String startTime, String endTime) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        if (loginUser != null) {
            wrapper.like(SysLoginLog::getUsername, loginUser);
        }
        if (status != null) {
            wrapper.eq(SysLoginLog::getStatus, status);
        }
        if (loginIp != null) {
            wrapper.like(SysLoginLog::getIpaddr, loginIp);
        }
        if (startTime != null) {
            wrapper.ge(SysLoginLog::getLoginTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(SysLoginLog::getLoginTime, endTime);
        }
        wrapper.orderByDesc(SysLoginLog::getLoginTime);

        List<SysLoginLog> loginLogs = baseMapper.selectList(wrapper);
        List<SysLoginLogVO> voList = new ArrayList<>();

        for (SysLoginLog loginLog : loginLogs) {
            SysLoginLogVO vo = new SysLoginLogVO();
            BeanUtils.copyProperties(loginLog, vo);
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public SysLoginLogVO getById(Long id) {
        SysLoginLog loginLog = baseMapper.selectById(id);
        if (loginLog != null) {
            SysLoginLogVO vo = new SysLoginLogVO();
            BeanUtils.copyProperties(loginLog, vo);
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
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        return remove(wrapper);
    }

    @Override
    public boolean saveLog(SysLoginLog sysLoginLog) {
        return save(sysLoginLog);
    }
}
