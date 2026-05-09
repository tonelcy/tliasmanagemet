package com.lcy.controller;

import com.lcy.pojo.ChangePasswordRequest;
import com.lcy.pojo.Emp;
import com.lcy.pojo.EmpQueryParam;
import com.lcy.pojo.PageResult;
import com.lcy.pojo.Result;
import com.lcy.service.EmpService;
import com.lcy.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/emps")
public class EmpController {


    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(//@RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam(defaultValue = "10") Integer pageSize,
//                       String name, Integer gender,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
                       EmpQueryParam empQueryParam
    ) {
        log.info("查询员工信息 page:{} pageSize:{} name:{} gender:{} begin:{} end:{}" + empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 添加员工
     */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("请求参数emp: {}", emp);
        empService.save(emp);
        return Result.success();
    }
    /**
     * 删除员工--数组
     */
    /*@DeleteMapping
    public Result delete(Integer[] ids){
        log.info("批量删除部门: ids={} ", ids.toString());
        empService.deleteByIds(ids);
        return Result.success();
    }*/

    /**
     * 批量删除员工--集合
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("批量删除部门: ids={} ", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 查询回显
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据id查询员工的详细信息");
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }
    /**
     * 更新员工信息
     */
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息, {}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/changePassword")
    public Result changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        log.info("修改密码请求");
        // 验证两次密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.error("两次输入的密码不一致");
        }
        // 获取当前登录用户ID
        Integer empId = CurrentHolder.getCurrentId();
        empService.changePassword(empId, request.getOldPassword(), request.getNewPassword());
        return Result.success();
    }
}
