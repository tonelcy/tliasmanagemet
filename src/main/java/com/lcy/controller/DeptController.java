package com.lcy.controller;

import java.util.List;

import com.lcy.anno.Log;
import com.lcy.pojo.Dept;
import com.lcy.pojo.Result;
import com.lcy.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门管理Controller
 */
@Slf4j
@RequestMapping("/depts")
@RestController
@Api(tags = "部门管理")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 查询部门列表
     */
    @GetMapping
    @ApiOperation("查询部门列表")
    public Result list() {
        log.info("查询部门列表");
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }
    /**
     * 根据id删除部门
     */
    @Log
    @DeleteMapping
    @ApiOperation("根据ID删除部门")
    public Result delete(@ApiParam("部门ID") Integer id) {
        log.info("删除部门id=" + id);
        deptService.deleteById(id);
        return Result.success();
    }
    /**
     * 添加部门
     */
    @Log
    @PostMapping
    @ApiOperation("新增部门")
    public Result save(@RequestBody Dept dept){
        log.info("添加部门, dept=" + dept);
        deptService.save(dept);
        return Result.success();
    }
    /**
     * 根据id查询部门
     */
    @Log
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询部门详情")
    public Result get(@ApiParam("部门ID") @PathVariable("id") Integer id) {
        log.info("查询部门id=" + id);
         Dept dept = deptService.getById(id);
         return Result.success(dept);
    }
    /**
     * 修改部门
     */
    @Log
    @PutMapping
    @ApiOperation("修改部门信息")
    public Result update(@RequestBody Dept dept){
        log.info("修改部门, dept=" + dept);
        deptService.update(dept);
        return Result.success();
    }
}
