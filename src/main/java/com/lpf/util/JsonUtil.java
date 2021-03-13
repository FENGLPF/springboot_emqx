package com.lpf.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * 对象转json字符串
     * @param value 对象
     * @return      json字符串
     * @throws JsonProcessingException
     */
    public static String objectToJsonStr(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    /**
     * json字符串转对象
     * @param jsonStr   json字符串
     * @param valueType 对象类型
     * @param <T>
     * @return          对象
     * @throws JsonProcessingException
     */
    public static <T> T objectFromJsonStr(String jsonStr, Class<T> valueType) throws JsonProcessingException {
        T obj = objectMapper.readValue(jsonStr, valueType);
        return obj;
    }

    public static Map<String, String> stringToMap(String jsonStr) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map = gson.fromJson(jsonStr, map.getClass());
        return map;
    }

}
