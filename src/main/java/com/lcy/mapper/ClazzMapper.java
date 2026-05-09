package com.lcy.mapper;

import com.lcy.pojo.Clazz;
import com.lcy.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 班级Mapper
 */
@Mapper
public interface ClazzMapper {

    /**
     * 条件查询
     */
    List<Clazz> list(ClazzQueryParam param);

    /**
     * 根据ID查询
     */
    @Select("select c.*, col.name as college_name from clazz c left join college col on c.college_id = col.id where c.id = #{id}")
    Clazz getById(Integer id);

    /**
     * 新增
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into clazz(name, room, college_id, head_teacher_id, create_time, update_time) " +
            "values(#{name}, #{room}, #{collegeId}, #{headTeacherId}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    /**
     * 修改
     */
    void update(Clazz clazz);

    /**
     * 删除
     */
    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 查询所有
     */
    @Select("select * from clazz")
    List<Clazz> findAll();
}
