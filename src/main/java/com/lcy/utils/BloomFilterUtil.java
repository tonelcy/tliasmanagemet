package com.lcy.utils;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class BloomFilterUtil {

    @Autowired
    @Qualifier("deptBloomFilter")
    private BloomFilter<String> deptBloomFilter;

    @Autowired
    @Qualifier("empBloomFilter")
    private BloomFilter<String> empBloomFilter;

    public boolean mightContainDept(String deptId) {
        return deptBloomFilter.mightContain(deptId);
    }

    public boolean mightContainEmp(String empId) {
        return empBloomFilter.mightContain(empId);
    }

    public void addDept(String deptId) {
        deptBloomFilter.put(deptId);
        log.info("添加部门ID到布隆过滤器: {}", deptId);
    }

    public void addEmp(String empId) {
        empBloomFilter.put(empId);
        log.info("添加员工ID到布隆过滤器: {}", empId);
    }

    public void batchAddDept(List<String> deptIds) {
        deptIds.forEach(deptBloomFilter::put);
        log.info("批量添加部门ID到布隆过滤器: {}", deptIds.size());
    }

    public void batchAddEmp(List<String> empIds) {
        empIds.forEach(empBloomFilter::put);
        log.info("批量添加员工ID到布隆过滤器: {}", empIds.size());
    }
}
