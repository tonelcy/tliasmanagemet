package com.lcy.service.impl;

import com.lcy.mapper.CollegeMapper;
import com.lcy.pojo.College;
import com.lcy.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学院Service实现
 */
@Slf4j
@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;

    @Override
    @Cacheable(value = "college", key = "'all'")
    public List<College> list() {
        log.info("查询所有学院，从数据库获取");
        return collegeMapper.list();
    }

    @Override
    @Cacheable(value = "college", key = "#id")
    public College getById(Integer id) {
        log.info("查询学院详情，从数据库获取: id={}", id);
        return collegeMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "college", allEntries = true)
    public void save(College college) {
        log.info("新增学院，清除缓存: {}", college);
        college.setCreateTime(LocalDateTime.now());
        college.setUpdateTime(LocalDateTime.now());
        collegeMapper.insert(college);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "college", allEntries = true)
    public void update(College college) {
        log.info("更新学院，清除缓存: {}", college);
        college.setUpdateTime(LocalDateTime.now());
        collegeMapper.update(college);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "college", allEntries = true)
    public void deleteById(Integer id) {
        log.info("删除学院，清除缓存: id={}", id);
        collegeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "college", allEntries = true)
    public void deleteByIds(List<Integer> ids) {
        log.info("批量删除学院，清除缓存: ids={}", ids);
        collegeMapper.deleteByIds(ids);
    }

    @Override
    public Integer count() {
        return collegeMapper.count();
    }
}
