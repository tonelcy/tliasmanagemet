package com.lcy.mapper;

import com.lcy.pojo.College;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 学院Mapper
 */
@Mapper
public interface CollegeMapper {

    /**
     * 查询所有
     */
    @Select("select * from college order by create_time desc")
    List<College> list();

    /**
     * 根据ID查询
     */
    @Select("select * from college where id = #{id}")
    College getById(Integer id);

    /**
     * 新增
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into college(name, code, dean, phone, address, create_time, update_time) " +
            "values(#{name}, #{code}, #{dean}, #{phone}, #{address}, #{createTime}, #{updateTime})")
    void insert(College college);

    /**
     * 修改
     */
    void update(College college);

    /**
     * 删除
     */
    @Delete("delete from college where id = #{id}")
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 统计学院数量
     */
    @Select("select count(*) from college")
    Integer count();
}
