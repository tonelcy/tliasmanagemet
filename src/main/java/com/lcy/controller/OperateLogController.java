package com.lcy.controller;

import com.lcy.pojo.OperateLog;
import com.lcy.pojo.OperateLogQueryParam;
import com.lcy.pojo.PageResult;
import com.lcy.pojo.Result;
import com.lcy.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志 Controller
 */
@Slf4j
@RestController
@RequestMapping("/operateLog")
@Api(tags = "操作日志管理")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    @GetMapping
    @ApiOperation("分页查询操作日志")
    public Result page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("操作人ID") @RequestParam(required = false) Integer operateEmpId,
            @ApiParam("开始时间") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String begin,
            @ApiParam("结束时间") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String end) {

        OperateLogQueryParam param = new OperateLogQueryParam();
        param.setPage(page);
        param.setPageSize(pageSize);
        param.setOperateEmpId(operateEmpId);
        if (begin != null) {
            param.setBegin(java.time.LocalDateTime.parse(begin.replace(" ", "T")));
        }
        if (end != null) {
            param.setEnd(java.time.LocalDateTime.parse(end.replace(" ", "T")));
        }

        log.info("分页查询操作日志: {}", param);
        PageResult<OperateLog> pageResult = operateLogService.page(param);
        return Result.success(pageResult);
    }
}
