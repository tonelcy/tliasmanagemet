package com.lcy.service;

import com.lcy.pojo.OperateLog;
import com.lcy.pojo.OperateLogQueryParam;
import com.lcy.pojo.PageResult;

public interface OperateLogService {
    /**
     * Insert operation log
     * @param operateLog Operation log object
     */
    void insert(OperateLog operateLog);

    /**
     * 分页查询操作日志
     */
    PageResult<OperateLog> page(OperateLogQueryParam param);
}
