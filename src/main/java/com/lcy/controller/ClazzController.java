package com.lcy.controller;

import com.lcy.pojo.Clazz;
import com.lcy.pojo.ClazzQueryParam;
import com.lcy.pojo.PageResult;
import com.lcy.pojo.Result;
import com.lcy.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级Controller
 */
@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询
     */
    @GetMapping
    public Result page(ClazzQueryParam param) {
        log.info("分页查询班级：{}", param);
        PageResult<Clazz> pageResult = clazzService.page(param);
        return Result.success(pageResult);
    }

    /**
     * 查询所有
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询所有班级");
        List<Clazz> list = clazzService.listAll();
        return Result.success(list);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询班级：{}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    /**
     * 新增
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级：{}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级：{}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 删除
     * 注意：调用方必须发送 DELETE 请求，Content-Type 为 application/json，body 为 JSON 数组，例如 [1,2,3]
     */
    @DeleteMapping
    public Result delete(@RequestBody List<Integer> ids) {
        log.info("删除班级：{}", ids);
        if (ids.size() == 1) {
            clazzService.deleteById(ids.get(0));
        } else {
            clazzService.deleteByIds(ids);
        }
        return Result.success();
    }
}
