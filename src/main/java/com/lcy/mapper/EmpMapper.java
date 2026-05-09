package com.lcy.mapper;

import com.lcy.pojo.Emp;
import com.lcy.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

//原始分页查询
//    /**
//     * 查询记录数
//     */
//    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
//    public Long count();
//
//    /**
//     * 查询所有的员工及其对应的部门名称
//     */
//    @Select("select e.*, d.name as dept_name from emp e left join dept d on e.dept_id = d.id " +
//            "order by e.update_time desc limit #{start},#{pageSize}")
//    public List<Emp> list(@Param("start") Integer start,@Param("pageSize") Integer pageSize);

//    /**
//     * 查询所有的员工及其对应的部门名称
//     */
//    @Select("select e.*, d.name deptName from emp as e left join dept as d on e.dept_id = d.id")
//    public List<Emp> list();

    /**
     * 查询所有的员工及其对应的部门名称
     */
    List<Emp> list(EmpQueryParam empQueryParam);
    /**
     * 添加员工
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")//获取到生成的主键--主键返回
    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    /**
     * 根据用户名查询用户
     */
    @Select("select * from emp where username = #{username}")
    Emp getByUsername(String username);
    /**
     * 批量删除员工
     */
    void deleteByIds(@Param("ids") List<Integer> ids);
    /**
     * 根据ID查询员工信息
     */
    Emp getById(Integer id);
    /**
     * 更新员工基本信息
     */
    void updateById(Emp emp);
    /**
     * 统计各个职位的员工人数
     */
    @MapKey("pos")
    List<Map<String,Object>> countEmpJobData();
    /**
     * 统计各个性别的员工人数
     */
    @MapKey("name")
    List<Map> countEmpGenderData();

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUsernameAndPassword(Emp emp);
}
