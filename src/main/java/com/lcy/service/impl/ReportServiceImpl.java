package com.lcy.service.impl;

import com.lcy.mapper.EmpMapper;
import com.lcy.pojo.JobOption;
import com.lcy.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    @Cacheable(value = "report", key = "'emp:job'")
    public JobOption getEmpJobData() {
        log.info("统计员工职位，从数据库获取");
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).collect(Collectors.toList());
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).collect(Collectors.toList());
        return new JobOption(jobList, dataList);
    }

    @Override
    @Cacheable(value = "report", key = "'emp:gender'")
    public List<Map> getEmpGenderData() {
        log.info("统计员工性别，从数据库获取");
        return empMapper.countEmpGenderData();
    }
}
