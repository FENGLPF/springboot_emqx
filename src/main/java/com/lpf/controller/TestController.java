package com.lpf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lpf.aop.SystemControllerLog;
import com.lpf.core.vo.CommonResult;
import com.lpf.service.TestDbService;
import com.lpf.util.JsonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Resource
    private TestDbService testDbService;

    @GetMapping(value = "/1")
    public String test1() {
        return "OK";
    }

    @SystemControllerLog(description = "测试mybatis", actionType = "1")
    @GetMapping(value = "/2")
    public CommonResult<String> test2() throws JsonProcessingException {
        List<Map<String, Object>> list = testDbService.selectTestDB();
        return new CommonResult<>(true,200,JsonUtil.objectToJsonStr(list),"success");
    }

}
