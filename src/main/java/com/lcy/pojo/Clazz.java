package com.lcy.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 班级实体类
 */
@Data
public class Clazz {
    private Integer id;
    private String name;
    private String room;
    private Integer collegeId;
    private String collegeName;
    private Integer headTeacherId;
    private String headTeacherName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
