package com.lpf.service;

import com.lpf.model.MqttMessage;

public interface MqttService {
    int insertMqttMessage(MqttMessage mqttMessage);
}
