package com.lcy.mapper;

import com.lcy.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询全部部门
     */
    //1.手动结果映射
//    @Results({
//            @Result(property = "createTime", column = "create_time"),
//            @Result(property = "updateTime", column = "update_time")
//    })
    //2.起别名
    //3.驼峰命名（推荐）

    @Select("select id, name,create_time,update_time from dept order by update_time desc")
    List<Dept> list();
    /**
     * 删除部门
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);
    /**
     * 添加部门
     */
    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);
    /**
     * 根据id查询部门
     */
    @Select("select id, name,create_time,update_time from dept where id = #{id}")
    Dept getById(Integer id);
    /**
     * 修改部门
     */
    @Update("update dept set name = #{name},create_time = #{createTime},update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
