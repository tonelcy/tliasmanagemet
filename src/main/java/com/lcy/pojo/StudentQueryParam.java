package com.lcy.pojo;

import lombok.Data;
import java.time.LocalDate;

/**
 * 学生查询参数
 */
@Data
public class StudentQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String name;
    private Integer clazzId;
    private Integer collegeId;
    private Integer degree;
    private LocalDate begin;
    private LocalDate end;
}
