package com.lcy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcy.mapper.StudentMapper;
import com.lcy.pojo.PageResult;
import com.lcy.pojo.Student;
import com.lcy.pojo.StudentQueryParam;
import com.lcy.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 学生Service实现
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Cacheable(value = "student", key = "'page:' + #param.page + ':' + #param.pageSize + ':' + #param.name + ':' + #param.degree + ':' + #param.clazzId")
    public PageResult<Student> page(StudentQueryParam param) {
        log.info("分页查询学生，从数据库获取: page={}, pageSize={}", param.getPage(), param.getPageSize());
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Student> list = studentMapper.list(param);
        Page<Student> p = (Page<Student>) list;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    @Cacheable(value = "student", key = "'id:' + #id")
    public Student getById(Integer id) {
        log.info("查询学生详情，从数据库获取: id={}", id);
        return studentMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "student", allEntries = true)
    public void save(Student student) {
        log.info("新增学生: {}", student);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "student", allEntries = true)
    public void update(Student student) {
        log.info("更新学生: {}", student);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "student", allEntries = true)
    public void deleteById(Integer id) {
        log.info("删除学生: id={}", id);
        studentMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "student", allEntries = true)
    public void deleteByIds(List<Integer> ids) {
        log.info("批量删除学生: ids={}", ids);
        studentMapper.deleteByIds(ids);
    }

    @Override
    public Integer count() {
        return studentMapper.count();
    }

    @Override
    @Cacheable(value = "student", key = "'gender'")
    public List<Map> countStudentGender() {
        log.info("统计学生性别，从数据库获取");
        return studentMapper.countStudentGender();
    }

    @Override
    @Cacheable(value = "student", key = "'degree'")
    public List<Map> countStudentDegree() {
        log.info("统计学生学历，从数据库获取");
        return studentMapper.countStudentDegree();
    }

    @Override
    @Cacheable(value = "student", key = "'college'")
    public List<Map> countStudentByCollege() {
        log.info("统计各学院学生，从数据库获取");
        return studentMapper.countStudentByCollege();
    }

    @Override
    @Cacheable(value = "student", key = "'clazz'")
    public List<Map> countStudentByClazz() {
        log.info("统计各班级学生，从数据库获取");
        return studentMapper.countStudentByClazz();
    }
}
