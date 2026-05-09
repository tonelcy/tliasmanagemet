package com.lcy.controller;

import com.lcy.pojo.PageResult;
import com.lcy.pojo.Result;
import com.lcy.pojo.Student;
import com.lcy.pojo.StudentQueryParam;
import com.lcy.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生Controller
 */
@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询
     */
    @GetMapping
    public Result page(StudentQueryParam param) {
        log.info("分页查询学生：{}", param);
        PageResult<Student> pageResult = studentService.page(param);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询学生：{}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 新增
     */
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学生：{}", student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学生：{}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除学生：{}", ids);
        if (ids.size() == 1) {
            studentService.deleteById(ids.get(0));
        } else {
            studentService.deleteByIds(ids);
        }
        return Result.success();
    }

    /**
     * 统计学生数量
     */
    @GetMapping("/count")
    public Result count() {
        log.info("统计学生数量");
        Integer count = studentService.count();
        return Result.success(count);
    }

    /**
     * 统计学生性别分布
     */
    @GetMapping("/statistics/gender")
    public Result countStudentGender() {
        log.info("统计学生性别分布");
        List<Map> list = studentService.countStudentGender();
        return Result.success(list);
    }

    /**
     * 统计学生学历分布
     */
    @GetMapping("/statistics/degree")
    public Result countStudentDegree() {
        log.info("统计学生学历分布");
        List<Map> list = studentService.countStudentDegree();
        return Result.success(list);
    }

    /**
     * 统计各学院学生数量
     */
    @GetMapping("/statistics/college")
    public Result countStudentByCollege() {
        log.info("统计各学院学生数量");
        List<Map> list = studentService.countStudentByCollege();
        return Result.success(list);
    }

    /**
     * 统计各班级学生数量
     */
    @GetMapping("/statistics/clazz")
    public Result countStudentByClazz() {
        log.info("统计各班级学生数量");
        List<Map> list = studentService.countStudentByClazz();
        return Result.success(list);
    }
}
