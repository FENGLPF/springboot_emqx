package com.lpf.service;

import com.lpf.dao.TestDbDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TestDbServiceImpl implements TestDbService {

    @Resource
    private TestDbDao testDbDao;

    @Override
    public List<Map<String, Object>> selectTestDB() {
        return testDbDao.selectTestDB();
    }
}
