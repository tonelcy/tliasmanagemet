package com.lcy.pojo;

import lombok.Data;
import java.time.LocalDate;

/**
 * 班级查询参数
 */
@Data
public class ClazzQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String name;
    private Integer collegeId;
    private LocalDate begin;
    private LocalDate end;
}
