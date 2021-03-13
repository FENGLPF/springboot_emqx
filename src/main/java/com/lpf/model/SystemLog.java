package com.lpf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SystemLog {

    private Long logId;

    private String logName;

    private String content;

    private String actionMethod;

    private String actionResult;

    private String logIp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

}
