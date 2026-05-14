package com.lcy.controller;

import com.lcy.pojo.Result;
import com.lcy.utils.CacheMetrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Api(tags = "缓存管理")
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheMetrics cacheMetrics;

    @ApiOperation("获取缓存指标")
    @GetMapping("/metrics")
    public Result getMetrics() {
        Map<String, Object> metrics = cacheMetrics.getMetrics();
        return Result.success(metrics);
    }

    @ApiOperation("重置缓存指标")
    @PostMapping("/metrics/reset")
    public Result resetMetrics() {
        cacheMetrics.resetMetrics();
        return Result.success();
    }

    @ApiOperation("打印缓存指标")
    @PostMapping("/metrics/log")
    public Result logMetrics() {
        cacheMetrics.logMetrics();
        return Result.success();
    }
}
