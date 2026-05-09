package com.lcy.controller;

import com.lcy.pojo.Emp;
import com.lcy.pojo.RegisterRequest;
import com.lcy.pojo.Result;
import com.lcy.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
public class RegisterController {
    @Autowired
    private EmpService empService;

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册，用户名：{}", request.getUsername());
        Emp emp = empService.register(request.getUsername(), request.getPassword());
        
        // 返回用户信息（不包含密码）
        emp.setPassword(null);
        return Result.success(emp);
    }
}
