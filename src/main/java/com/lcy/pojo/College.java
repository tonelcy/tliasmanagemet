package com.lcy.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学院实体类
 */
@Data
public class College {
    private Integer id;
    private String name;
    private String code;
    private String dean;
    private String phone;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
