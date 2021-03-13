package com.lpf.service;

import com.lpf.dao.MqttMessageDao;
import com.lpf.model.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MqttServiceImpl implements MqttService {

    @Resource
    private MqttMessageDao mqttMessageDao;

    @Override
    public int insertMqttMessage(MqttMessage mqttMessage) {
        return mqttMessageDao.insertMqttMessage(mqttMessage);
    }
}
