package com.lcy.mapper;

import com.lcy.pojo.EmpExpr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    void insertBatch(@Param("exprList") List<EmpExpr> exprList);

    void deleteByEmpIds(@Param("empIds") List<Integer> ids);
}
