package com.lcy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcy.exception.BusinessException;
import com.lcy.mapper.EmpExprMapper;
import com.lcy.mapper.EmpLogMapper;
import com.lcy.mapper.EmpMapper;
import com.lcy.pojo.*;
import com.lcy.service.EmpLogService;
import com.lcy.service.EmpService;
import com.lcy.utils.JwtUtils;
import com.lcy.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private EmpLogService empLogService;
    @Autowired
    private EmpLogMapper empLogMapper;

    @Override
    public List<Emp> list() {
        return empMapper.list(null);
    }

    @Cacheable(value = "emp", key = "'page:' + #empQueryParam.page + ':' + #empQueryParam.pageSize")
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        log.info("分页查询员工，从数据库获取: page={}, pageSize={}", empQueryParam.getPage(), empQueryParam.getPageSize());
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> empList = empMapper.list(empQueryParam);
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = "emp", allEntries = true)
    public void save(Emp emp) {
        log.info("新增员工，清除缓存: {}", emp);
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), emp.toString());
            empLogService.insert(empLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = "emp", allEntries = true)
    public void deleteByIds(List<Integer> ids) {
        log.info("删除员工，清除缓存: ids={}", ids);
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @Cacheable(value = "emp", key = "'info:' + #id")
    public Emp getInfo(Integer id) {
        log.info("查询员工详情，从数据库获取: id={}", id);
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = "emp", allEntries = true)
    public void update(Emp emp) {
        log.info("更新员工，清除缓存: {}", emp);
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp empLogin = empMapper.getByUsername(emp.getUsername());
        if (empLogin == null) {
            return null;
        }

        boolean passwordMatch = false;
        if (isBCryptPassword(empLogin.getPassword())) {
            passwordMatch = PasswordUtils.matches(emp.getPassword(), empLogin.getPassword());
        } else {
            passwordMatch = emp.getPassword().equals(empLogin.getPassword());
            if (passwordMatch) {
                empLogin.setPassword(PasswordUtils.encode(emp.getPassword()));
                empMapper.updateById(empLogin);
            }
        }

        if (passwordMatch) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", empLogin.getId());
            dataMap.put("username", empLogin.getUsername());
            String jwt = JwtUtils.generateJwt(dataMap);
            LoginInfo loginInfo = new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), jwt);
            return loginInfo;
        }
        return null;
    }

    private boolean isBCryptPassword(String password) {
        return password != null && (password.startsWith("$2a$") ||
                password.startsWith("$2b$") ||
                password.startsWith("$2y$"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Emp register(String username, String password) {
        Emp existingEmp = empMapper.getByUsername(username);
        if (existingEmp != null) {
            throw new BusinessException("用户名已存在");
        }

        if (password.length() < 6) {
            throw new BusinessException("密码长度不能少于6位");
        }

        Emp emp = new Emp();
        emp.setUsername(username);
        emp.setPassword(PasswordUtils.encode(password));
        emp.setName(username);
        emp.setGender(1);
        emp.setPhone("00000000000");
        emp.setJob(5);
        emp.setSalary(0);
        emp.setImage("");
        emp.setEntryDate(LocalDate.now());
        emp.setDeptId(1);
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.insert(emp);
        return emp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Integer empId, String oldPassword, String newPassword) {
        Emp emp = empMapper.getById(empId);
        if (emp == null) {
            throw new BusinessException("员工不存在");
        }

        boolean passwordMatch = false;
        if (isBCryptPassword(emp.getPassword())) {
            passwordMatch = PasswordUtils.matches(oldPassword, emp.getPassword());
        } else {
            passwordMatch = oldPassword.equals(emp.getPassword());
        }

        if (!passwordMatch) {
            throw new BusinessException("当前密码错误");
        }

        emp.setPassword(PasswordUtils.encode(newPassword));
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
    }

}
