package com.lcy.controller;

import com.lcy.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "连接测试")
@RestController
@RequestMapping("/test")
public class ConnectionTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("测试Redis连接")
    @GetMapping("/redis")
    public Result testRedis() {
        Map<String, Object> result = new HashMap<>();
        try {
            String testKey = "test:connection";
            String testValue = "Hello Redis!";
            
            // 写入测试
            redisTemplate.opsForValue().set(testKey, testValue);
            log.info("Redis写入成功");
            
            // 读取测试
            Object readValue = redisTemplate.opsForValue().get(testKey);
            log.info("Redis读取成功: {}", readValue);
            
            // 删除测试
            redisTemplate.delete(testKey);
            
            result.put("status", "success");
            result.put("message", "Redis连接正常");
            result.put("testKey", testKey);
            result.put("testValue", testValue);
            result.put("readValue", readValue);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("Redis连接测试失败", e);
            result.put("status", "error");
            result.put("message", "Redis连接失败: " + e.getMessage());
            return Result.error("Redis连接失败: " + e.getMessage());
        }
    }
}
