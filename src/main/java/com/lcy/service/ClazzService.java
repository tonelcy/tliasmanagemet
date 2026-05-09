package com.lcy.service;

import com.lcy.pojo.Clazz;
import com.lcy.pojo.ClazzQueryParam;
import com.lcy.pojo.PageResult;

import java.util.List;

/**
 * 班级Service接口
 */
public interface ClazzService {
    PageResult<Clazz> page(ClazzQueryParam param);
    Clazz getById(Integer id);
    void save(Clazz clazz);
    void update(Clazz clazz);
    void deleteById(Integer id);
    void deleteByIds(List<Integer> ids);
    List<Clazz> listAll();
}
