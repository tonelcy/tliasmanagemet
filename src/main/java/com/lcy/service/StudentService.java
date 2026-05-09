package com.lcy.service;

import com.lcy.pojo.PageResult;
import com.lcy.pojo.Student;
import com.lcy.pojo.StudentQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 学生Service接口
 */
public interface StudentService {
    PageResult<Student> page(StudentQueryParam param);
    Student getById(Integer id);
    void save(Student student);
    void update(Student student);
    void deleteById(Integer id);
    void deleteByIds(List<Integer> ids);
    Integer count();
    List<Map> countStudentGender();
    List<Map> countStudentDegree();
    List<Map> countStudentByCollege();
    List<Map> countStudentByClazz();
}
