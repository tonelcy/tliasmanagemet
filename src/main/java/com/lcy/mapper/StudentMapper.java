package com.lcy.mapper;

import com.lcy.pojo.Student;
import com.lcy.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 学生Mapper
 */
@Mapper
public interface StudentMapper {

    /**
     * 条件查询
     */
    List<Student> list(StudentQueryParam param);

    /**
     * 根据ID查询
     */
    @Select("select s.*, c.name as clazz_name, col.name as college_name " +
            "from student s left join clazz c on s.clazz_id = c.id " +
            "left join college col on s.college_id = col.id where s.id = #{id}")
    Student getById(Integer id);

    /**
     * 新增
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into student(name, no, gender, phone, clazz_id, college_id, degree, admission_date, image, create_time, update_time) " +
            "values(#{name}, #{no}, #{gender}, #{phone}, #{clazzId}, #{collegeId}, #{degree}, #{admissionDate}, #{image}, #{createTime}, #{updateTime})")
    void insert(Student student);

    /**
     * 修改
     */
    void update(Student student);

    /**
     * 删除
     */
    @Delete("delete from student where id = #{id}")
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 统计学生数量
     */
    @Select("select count(*) from student")
    Integer count();

    /**
     * 统计学生性别分布
     */
    @MapKey("name")
    List<Map> countStudentGender();

    /**
     * 统计学生学历分布
     */
    @MapKey("name")
    List<Map> countStudentDegree();

    /**
     * 统计各学院学生数量
     */
    @MapKey("name")
    List<Map> countStudentByCollege();

    /**
     * 统计各班级学生数量
     */
    @MapKey("name")
    List<Map> countStudentByClazz();
}
