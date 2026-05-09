package com.lcy.service.impl;

import com.lcy.mapper.DeptMapper;
import com.lcy.pojo.Dept;
import com.lcy.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Cacheable(value = "dept", key = "'all'")
    public List<Dept> list() {
        log.info("查询所有部门，从数据库获取");
        return deptMapper.list();
    }

    @CacheEvict(value = "dept", allEntries = true)
    public void deleteById(Integer id) {
        log.info("删除部门，清除缓存: id={}", id);
        deptMapper.deleteById(id);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public void save(Dept dept) {
        log.info("新增部门，清除缓存: {}", dept);
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    @Cacheable(value = "dept", key = "#id")
    public Dept getById(Integer id) {
        log.info("查询部门，从数据库获取: id={}", id);
        return deptMapper.getById(id);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public void update(Dept dept) {
        log.info("更新部门，清除缓存: {}", dept);
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

}
