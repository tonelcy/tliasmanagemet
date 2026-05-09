package com.lcy.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.lcy.mapper.DeptMapper;
import com.lcy.mapper.EmpMapper;
import com.lcy.pojo.EmpQueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class BloomFilterConfig {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmpMapper empMapper;

    @Bean("deptBloomFilter")
    public BloomFilter<String> deptBloomFilter() {
        BloomFilter<String> bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                1000,
                0.001
        );
        deptMapper.list().forEach(dept -> bloomFilter.put(String.valueOf(dept.getId())));
        return bloomFilter;
    }

    @Bean("empBloomFilter")
    public BloomFilter<String> empBloomFilter() {
        BloomFilter<String> bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                10000,
                0.001
        );
        empMapper.list(null).forEach(emp -> bloomFilter.put(String.valueOf(emp.getId())));
        return bloomFilter;
    }
}
