package com.lcy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcy.mapper.OperateLogMapper;
import com.lcy.pojo.OperateLog;
import com.lcy.pojo.OperateLogQueryParam;
import com.lcy.pojo.PageResult;
import com.lcy.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Transactional
    @Override
    public void insert(OperateLog operateLog) {
        operateLogMapper.insert(operateLog);
    }

    @Override
    public PageResult<OperateLog> page(OperateLogQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<OperateLog> list = operateLogMapper.list(param);
        Page<OperateLog> p = (Page<OperateLog>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
