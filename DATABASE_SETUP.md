# Tlias 数据库初始化与数据填充指南

## 📋 概述

本文档详细说明了如何初始化和填充 tlias 数据库，确保所有功能模块正常运行。

## 📦 数据库结构

### 核心数据表

| 表名 | 说明 | 记录数 |
|-----|------|--------|
| `college` | 学院表 | 5条 |
| `clazz` | 班级表 | 6条 |
| `student` | 学生表 | 20条 |
| `dept` | 部门表 | (现有) |
| `emp` | 员工表 | (现有) |
| `operate_log` | 操作日志表 | (现有) |
| `emp_expr` | 员工经历表 | (现有) |
| `emp_log` | 员工日志表 | (现有) |

## 🚀 快速开始

### 步骤 1: 执行 SQL 脚本

在 MySQL 中执行以下文件：
```
tlias-database-init.sql
```

执行方式：
```bash
mysql -u root -p tlias < tlias-database-init.sql
```
或者在 MySQL 客户端（如 Navicat、DBeaver）中直接执行。

### 步骤 2: 验证数据

执行脚本后，运行以下查询验证数据：

```sql
-- 查看各表数据量
SELECT 'college' AS table_name, COUNT(*) AS record_count FROM college
UNION ALL
SELECT 'clazz', COUNT(*) FROM clazz
UNION ALL
SELECT 'student', COUNT(*) FROM student
UNION ALL
SELECT 'dept', COUNT(*) FROM dept
UNION ALL
SELECT 'emp', COUNT(*) FROM emp;
```

### 步骤 3: 测试登录

**登录账号密码**（所有用户统一密码：`123456`）
- 原有账号：使用数据库中现有的用户名
- 新注册用户：自动使用 BCrypt 加密

## 📊 数据填充详情

### 1. 学院数据 (college)

| ID | 学院名称 | 代码 | 院长 | 电话 | 地址 |
|----|---------|------|------|------|------|
| 1 | 计算机学院 | CS | 张明 | 13800138001 | 教学楼A座101 |
| 2 | 数学学院 | MATH | 李华 | 13800138002 | 教学楼B座201 |
| 3 | 外国语学院 | FL | 王芳 | 13800138003 | 教学楼C座301 |
| 4 | 经济管理学院 | EM | 刘强 | 13800138004 | 教学楼D座401 |
| 5 | 艺术设计学院 | AD | 赵静 | 13800138005 | 艺术楼101 |

### 2. 班级数据 (clazz)

| ID | 班级名称 | 教室 | 学院ID | 班主任ID |
|----|---------|------|--------|---------|
| 1 | 计算机科学与技术1班 | A101 | 1 | 1 |
| 2 | 计算机科学与技术2班 | A102 | 1 | 2 |
| 3 | 软件工程1班 | A103 | 1 | 3 |
| 4 | 数学与应用数学1班 | B201 | 2 | 4 |
| 5 | 英语1班 | C301 | 3 | 5 |
| 6 | 金融1班 | D401 | 4 | 6 |

### 3. 学生数据 (student)

**性别分布**：
- 男生：11人
- 女生：9人

**学历分布**：
- 专科：3人
- 本科：12人
- 硕士：4人
- 博士：1人

**班级分布**：
- 计算机1班：3人
- 计算机2班：2人
- 软件工程1班：2人
- 数学1班：2人
- 英语1班：2人
- 金融1班：2人
- 其他：7人

**学生名单**：
张三、李四、王五、赵六、钱七、孙八、周九、吴十、郑十一、王十二、
李十三、张十四、刘十五、陈十六、杨十七、黄十八、赵十九、周二十、吴二十一、郑二十二

### 4. 员工密码更新

脚本会自动将以下密码更新为 BCrypt 加密格式：
- `123456` → BCrypt 加密
- `123` → BCrypt 加密
- `NULL` 或空字符串 → BCrypt 加密

所有账号的密码统一为：**123456**

## 🔐 安全性说明

### BCrypt 密码加密

- **加密强度**：10 rounds
- **密文长度**：60字符
- **示例密文**：`$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6zPXy`

### 数据库字段调整

```sql
-- emp表 password 字段从原来的长度扩展到 100
ALTER TABLE `emp` MODIFY COLUMN `password` varchar(100) NOT NULL COMMENT '密码';
```

## 🧪 接口测试

### 1. 学院管理模块

**查询所有学院**
```bash
GET /colleges
```

**新增学院**
```bash
POST /colleges
Content-Type: application/json
{
  "name": "测试学院",
  "code": "TEST",
  "dean": "测试院长",
  "phone": "13800138000",
  "address": "测试地址"
}
```

### 2. 班级管理模块

**分页查询**
```bash
GET /clazzs?page=1&pageSize=10
```

### 3. 学生管理模块

**分页查询**
```bash
GET /students?page=1&pageSize=10
```

### 4. 统计分析模块

**学生性别统计**
```bash
GET /students/statistics/gender
```

**学生学历统计**
```bash
GET /students/statistics/degree
```

**各学院学生统计**
```bash
GET /students/statistics/college
```

**各班级学生统计**
```bash
GET /students/statistics/clazz
```

## ⚠️ 注意事项

### 1. 现有数据保留

脚本不会删除现有的 `dept`、`emp`、`operate_log`、`emp_expr`、`emp_log` 表数据，只会：
- 更新 `emp` 表的 `password` 字段长度
- 将符合条件的明文密码更新为 BCrypt 加密格式

### 2. 数据关联关系

- `clazz.college_id` → `college.id`
- `clazz.head_teacher_id` → `emp.id`
- `student.clazz_id` → `clazz.id`
- `student.college_id` → `college.id`

### 3. 字符编码

所有表使用 `utf8mb4` 字符集，支持完整的 Unicode（包括 emoji）。

### 4. 字段约束

- `college.code`：唯一约束
- `student.no`：唯一约束
- 所有外键字段：建立索引优化查询

## 🔍 故障排查

### 问题 1：密码验证失败

**症状**：登录时提示 "Encoded password does not look like BCrypt"

**解决方案**：
```sql
-- 手动更新密码为BCrypt格式
UPDATE emp SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6zPXy' WHERE id = <用户ID>;
```

### 问题 2：新模块功能无法使用

**检查列表**：
1. 确认 `college`、`clazz`、`student` 表已创建
2. 确认后端服务已重启
3. 检查前端路由配置是否正确

### 问题 3：图表数据为空

**检查统计数据**：
```sql
SELECT * FROM student;
SELECT * FROM clazz;
SELECT * FROM college;
```

## 📞 技术支持

如遇问题，请检查：
1. MySQL 版本 >= 5.7
2. 数据库连接配置正确
3. 用户有足够的权限（CREATE, INSERT, UPDATE, ALTER）

---

**文档版本**：v1.0
**创建日期**：2026-05-06
**最后更新**：2026-05-06
