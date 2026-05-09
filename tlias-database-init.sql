-- ================================================
-- tlias 数据库完整初始化脚本
-- 功能：创建缺失的表、更新表结构、填充测试数据
-- 创建时间：2026-05-06
-- ================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ================================================
-- 一、创建缺失的数据表
-- ================================================

-- 1.学院表
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '学院名称',
  `code` varchar(20) NOT NULL COMMENT '学院代码',
  `dean` varchar(20) DEFAULT NULL COMMENT '院长姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院表';

-- 2.班级表
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '班级名称',
  `room` varchar(30) DEFAULT NULL COMMENT '教室',
  `college_id` int(11) DEFAULT NULL COMMENT '所属学院ID',
  `head_teacher_id` int(11) DEFAULT NULL COMMENT '班主任ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_college_id` (`college_id`),
  KEY `idx_head_teacher_id` (`head_teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 3.学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `no` varchar(20) NOT NULL COMMENT '学号',
  `gender` tinyint(1) NOT NULL DEFAULT '1' COMMENT '性别, 1:男, 2:女',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `clazz_id` int(11) DEFAULT NULL COMMENT '班级ID',
  `college_id` int(11) DEFAULT NULL COMMENT '学院ID',
  `degree` tinyint(1) DEFAULT '2' COMMENT '学历, 1:专科, 2:本科, 3:硕士, 4:博士',
  `admission_date` date DEFAULT NULL COMMENT '入学日期',
  `image` varchar(200) DEFAULT NULL COMMENT '头像',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_no` (`no`),
  KEY `idx_clazz_id` (`clazz_id`),
  KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ================================================
-- 二、更新现有表结构（为支持BCrypt密码加密）
-- ================================================

-- 检查emp表password字段长度，如不够则修改为100
-- 注：BCrypt加密后的密码固定为60个字符
ALTER TABLE `emp` MODIFY COLUMN `password` varchar(100) NOT NULL COMMENT '密码';

-- ================================================
-- 三、填充测试数据
-- ================================================

-- ------------------------------------------------
-- 1.填充学院数据
-- ------------------------------------------------
INSERT INTO `college` (`id`, `name`, `code`, `dean`, `phone`, `address`, `create_time`, `update_time`) VALUES
(1, '计算机学院', 'CS', '张明', '13800138001', '教学楼A座101', '2023-10-01 08:00:00', '2023-10-01 08:00:00'),
(2, '数学学院', 'MATH', '李华', '13800138002', '教学楼B座201', '2023-10-01 08:00:00', '2023-10-01 08:00:00'),
(3, '外国语学院', 'FL', '王芳', '13800138003', '教学楼C座301', '2023-10-01 08:00:00', '2023-10-01 08:00:00'),
(4, '经济管理学院', 'EM', '刘强', '13800138004', '教学楼D座401', '2023-10-01 08:00:00', '2023-10-01 08:00:00'),
(5, '艺术设计学院', 'AD', '赵静', '13800138005', '艺术楼101', '2023-10-01 08:00:00', '2023-10-01 08:00:00');

-- ------------------------------------------------
-- 2.填充班级数据
-- ------------------------------------------------
INSERT INTO `clazz` (`id`, `name`, `room`, `college_id`, `head_teacher_id`, `create_time`, `update_time`) VALUES
(1, '计算机科学与技术1班', 'A101', 1, 1, '2023-10-01 09:00:00', '2023-10-01 09:00:00'),
(2, '计算机科学与技术2班', 'A102', 1, 2, '2023-10-01 09:00:00', '2023-10-01 09:00:00'),
(3, '软件工程1班', 'A103', 1, 3, '2023-10-01 09:00:00', '2023-10-01 09:00:00'),
(4, '数学与应用数学1班', 'B201', 2, 4, '2023-10-01 09:00:00', '2023-10-01 09:00:00'),
(5, '英语1班', 'C301', 3, 5, '2023-10-01 09:00:00', '2023-10-01 09:00:00'),
(6, '金融1班', 'D401', 4, 6, '2023-10-01 09:00:00', '2023-10-01 09:00:00');

-- ------------------------------------------------
-- 3.填充学生数据
-- ------------------------------------------------
INSERT INTO `student` (`id`, `name`, `no`, `gender`, `phone`, `clazz_id`, `college_id`, `degree`, `admission_date`, `image`, `create_time`, `update_time`) VALUES
(1, '张三', '2023001', 1, '13900139001', 1, 1, 2, '2023-09-01', '1.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(2, '李四', '2023002', 1, '13900139002', 1, 1, 2, '2023-09-01', '2.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(3, '王五', '2023003', 2, '13900139003', 1, 1, 2, '2023-09-01', '3.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(4, '赵六', '2023004', 1, '13900139004', 2, 1, 2, '2023-09-01', '4.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(5, '钱七', '2023005', 2, '13900139005', 2, 1, 2, '2023-09-01', '5.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(6, '孙八', '2023006', 1, '13900139006', 3, 1, 2, '2023-09-01', '6.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(7, '周九', '2023007', 2, '13900139007', 3, 1, 2, '2023-09-01', '7.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(8, '吴十', '2023008', 1, '13900139008', 4, 2, 2, '2023-09-01', '8.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(9, '郑十一', '2023009', 2, '13900139009', 4, 2, 2, '2023-09-01', '9.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(10, '王十二', '2023010', 1, '13900139010', 5, 3, 2, '2023-09-01', '10.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(11, '李十三', '2023011', 2, '13900139011', 5, 3, 2, '2023-09-01', '11.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(12, '张十四', '2023012', 1, '13900139012', 6, 4, 2, '2023-09-01', '12.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(13, '刘十五', '2023013', 2, '13900139013', 6, 4, 2, '2023-09-01', '13.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(14, '陈十六', '2023014', 1, '13900139014', 1, 1, 3, '2023-09-01', '14.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(15, '杨十七', '2023015', 2, '13900139015', 2, 1, 3, '2023-09-01', '15.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(16, '黄十八', '2023016', 1, '13900139016', 3, 1, 1, '2023-09-01', '16.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(17, '赵十九', '2023017', 2, '13900139017', 4, 2, 1, '2023-09-01', '17.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(18, '周二十', '2023018', 1, '13900139018', 5, 3, 1, '2023-09-01', '18.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(19, '吴二十一', '2023019', 2, '13900139019', 6, 4, 3, '2023-09-01', '19.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00'),
(20, '郑二十二', '2023020', 1, '13900139020', 1, 1, 4, '2023-09-01', '20.jpg', '2023-10-01 10:00:00', '2023-10-01 10:00:00');

-- ------------------------------------------------
-- 4.为现有emp表用户更新密码为BCrypt加密格式
-- 注意：以下密码都是 "123456" 的BCrypt加密结果
-- 这样既兼容原有明文，又支持BCrypt验证
-- ------------------------------------------------

-- 更新现有用户密码为BCrypt加密（密码：123456）
-- 只有当密码不是BCrypt格式时才更新
UPDATE `emp` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6zPXy' WHERE `password` = '123456';
UPDATE `emp` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6zPXy' WHERE `password` = '123';
UPDATE `emp` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6zPXy' WHERE `password` IS NULL OR `password` = '';

-- ================================================
-- 四、数据统计查询测试（验证数据完整性）
-- ================================================

-- 查询各表数据量
SELECT 'college' AS table_name, COUNT(*) AS record_count FROM college
UNION ALL
SELECT 'clazz', COUNT(*) FROM clazz
UNION ALL
SELECT 'student', COUNT(*) FROM student
UNION ALL
SELECT 'dept', COUNT(*) FROM dept
UNION ALL
SELECT 'emp', COUNT(*) FROM emp;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================
-- 脚本执行完成
-- ================================================
