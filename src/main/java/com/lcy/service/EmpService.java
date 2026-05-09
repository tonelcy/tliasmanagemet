package com.lcy.service;

import com.lcy.pojo.Emp;
import com.lcy.pojo.EmpQueryParam;
import com.lcy.pojo.LoginInfo;
import com.lcy.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    /**
     * 查询所有员工信息
     */
    List<Emp> list();

    /**
     * 分页查询员工信息
     */
    PageResult page(EmpQueryParam empQueryParam);
    /**
     * 保存员工信息
     */
    void save(Emp emp);
    /**
     * 批量删除员工
     */
    void deleteByIds(List<Integer> ids);
    /**
     * 根据ID查询员工的详细信息
     */
    Emp getInfo(Integer id);
    /**
     * 更新员工信息
     */
    void update(Emp emp);

    /**
     *
     * 登录
     * @param emp
     * @return
     */
    LoginInfo login(Emp emp);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 注册的用户信息
     */
    Emp register(String username, String password);

    /**
     * 修改密码
     * @param empId 员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Integer empId, String oldPassword, String newPassword);
}
