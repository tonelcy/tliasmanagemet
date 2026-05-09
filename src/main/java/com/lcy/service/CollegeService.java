package com.lcy.service;

import com.lcy.pojo.College;
import com.lcy.pojo.PageResult;

import java.util.List;

/**
 * 学院Service接口
 */
public interface CollegeService {
    List<College> list();
    College getById(Integer id);
    void save(College college);
    void update(College college);
    void deleteById(Integer id);
    void deleteByIds(List<Integer> ids);
    Integer count();
}
