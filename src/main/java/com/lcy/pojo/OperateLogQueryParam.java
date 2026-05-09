package com.lcy.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志查询参数
 */
@Data
public class OperateLogQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer operateEmpId;
    private LocalDateTime begin;
    private LocalDateTime end;
}
