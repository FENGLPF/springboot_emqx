package com.lpf.dao;

import com.lpf.model.MqttMessage;

public interface MqttMessageDao {

    int insertMqttMessage(MqttMessage mqttMessage);

}
