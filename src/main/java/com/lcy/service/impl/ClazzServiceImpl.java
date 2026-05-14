package com.lcy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcy.mapper.ClazzMapper;
import com.lcy.pojo.Clazz;
import com.lcy.pojo.ClazzQueryParam;
import com.lcy.pojo.PageResult;
import com.lcy.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 班级Service实现
 */
@Slf4j
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    @Cacheable(value = "clazz", key = "'page:' + #param.page + ':' + #param.pageSize + ':' + #param.name + ':' + #param.begin + ':' + #param.end")
    public PageResult<Clazz> page(ClazzQueryParam param) {
        log.info("分页查询班级，从数据库获取: page={}, pageSize={}", param.getPage(), param.getPageSize());
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Clazz> list = clazzMapper.list(param);
        Page<Clazz> p = (Page<Clazz>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    @Cacheable(value = "clazz", key = "'id:' + #id")
    public Clazz getById(Integer id) {
        log.info("查询班级详情，从数据库获取: id={}", id);
        return clazzMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "clazz", allEntries = true)
    public void save(Clazz clazz) {
        log.info("新增班级: {}", clazz);
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "clazz", allEntries = true)
    public void update(Clazz clazz) {
        log.info("更新班级: {}", clazz);
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "clazz", allEntries = true)
    public void deleteById(Integer id) {
        log.info("删除班级: id={}", id);
        clazzMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "clazz", allEntries = true)
    public void deleteByIds(List<Integer> ids) {
        log.info("批量删除班级: ids={}", ids);
        clazzMapper.deleteByIds(ids);
    }

    @Override
    @Cacheable(value = "clazz", key = "'listAll'")
    public List<Clazz> listAll() {
        log.info("查询所有班级，从数据库获取");
        return clazzMapper.findAll();
    }
}
