package com.lpf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Employee {

    private Long empId;

    private Long comId;

    private String empName;

    private String empAccount;

    private String empPassword;

    private Integer empSex;

    private Integer empEnable;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

}
