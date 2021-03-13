package com.lpf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MqttMessage {

    private Long mqttId;

    private String topic;

    private String message;

    private Integer mqttQos;

    private Integer mqttReceivedRetained;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mqttTime;

}
