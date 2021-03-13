package com.lpf.service;

import com.lpf.dao.SystemLogDao;
import com.lpf.model.SystemLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Resource
    private SystemLogDao systemLogDao;

    @Override
    public int insertSystemLog(SystemLog systemLog) {
        return systemLogDao.insertSystemLog(systemLog);
    }
}
