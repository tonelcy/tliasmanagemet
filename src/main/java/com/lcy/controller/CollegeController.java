package com.lcy.controller;

import com.lcy.pojo.College;
import com.lcy.pojo.Result;
import com.lcy.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学院Controller
 */
@Slf4j
@RestController
@RequestMapping("/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    /**
     * 查询所有
     */
    @GetMapping
    public Result list() {
        log.info("查询所有学院");
        List<College> list = collegeService.list();
        return Result.success(list);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询学院：{}", id);
        College college = collegeService.getById(id);
        return Result.success(college);
    }

    /**
     * 新增
     */
    @PostMapping
    public Result save(@RequestBody College college) {
        log.info("新增学院：{}", college);
        collegeService.save(college);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping
    public Result update(@RequestBody College college) {
        log.info("修改学院：{}", college);
        collegeService.update(college);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除学院：{}", ids);
        if (ids.size() == 1) {
            collegeService.deleteById(ids.get(0));
        } else {
            collegeService.deleteByIds(ids);
        }
        return Result.success();
    }

    /**
     * 统计学院数量
     */
    @GetMapping("/count")
    public Result count() {
        log.info("统计学院数量");
        Integer count = collegeService.count();
        return Result.success(count);
    }
}
