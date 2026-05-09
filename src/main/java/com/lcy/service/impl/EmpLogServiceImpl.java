package com.lcy.service.impl;

import com.lcy.mapper.EmpLogMapper;
import com.lcy.pojo.EmpLog;
import com.lcy.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements EmpLogService {
    @Autowired
    private EmpLogMapper empLogMapper;

    @Transactional
    @Override
    public void insert(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
}
