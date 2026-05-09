package com.lcy.pojo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
public class Student {
    private Integer id;
    private String name;
    private String no;
    private Integer gender;
    private String phone;
    private Integer clazzId;
    private String clazzName;
    private Integer collegeId;
    private String collegeName;
    private Integer degree;
    private LocalDate admissionDate;
    private String image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
