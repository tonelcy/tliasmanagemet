package com.lcy.service;

import com.lcy.pojo.Dept;

import java.util.List;

public interface DeptService {
    /**
     * 查询部门列表
     */

    List<Dept> list();

    /**
     *根据id删除部门
     */
    void deleteById(Integer id);
    /**
     * 保存部门
     */
    void save(Dept dept);
    /**
     * 根据id查询部门
     */
    Dept getById(Integer id);
    /**
     * 更新部门
     */
    void update(Dept dept);
}
