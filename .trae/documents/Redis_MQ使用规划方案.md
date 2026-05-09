# Redis & MQ 完整使用规划方案

## 📋 项目概述

本方案为tlias-web-management项目提供完整的Redis和消息队列(MQ)使用规划，包括部署配置、代码实现、验证标准等内容。

---

## 🎯 一、Redis使用规划

### 1.1 Redis部署方案

由于Windows系统的Redis版本限制，提供以下几种部署方案：

#### 方案A：Linux云服务器部署（推荐）
**适用场景**：团队协作开发、生产环境

**实施步骤**：
1. 购买Linux云服务器（阿里云/腾讯云等）
2. 安装Redis
3. 配置远程访问
4. 安全组放行

#### 方案B：本地虚拟机部署
**适用场景**：个人学习、本地开发

**实施步骤**：
1. 安装VMware/VirtualBox
2. 安装Linux发行版（推荐Ubuntu/CentOS）
3. 在Linux中安装Redis

#### 方案C：使用Docker（跨平台）
**适用场景**：快速部署、环境一致

**实施步骤**：
1. 安装Docker Desktop
2. 拉取Redis镜像
3. 启动Redis容器

### 1.2 详细部署步骤

#### 方案A：Ubuntu系统（推荐，最简单）

##### 方式1：使用apt包管理器安装
```bash
# 1. 更新系统包
sudo apt update && sudo apt upgrade -y

# 2. 安装Redis
sudo apt install -y redis-server

# 3. 启动Redis
sudo systemctl start redis-server

# 4. 设置开机自启
sudo systemctl enable redis-server

# 5. 检查状态
sudo systemctl status redis-server
```

##### 方式2：编译安装（获取最新版本）
```bash
# 1. 更新系统包
sudo apt update && sudo apt upgrade -y

# 2. 安装编译工具
sudo apt install -y build-essential pkg-config

# 3. 下载Redis（最新稳定版）
wget https://download.redis.io/releases/redis-7.0.12.tar.gz

# 4. 解压
tar -zxvf redis-7.0.12.tar.gz
cd redis-7.0.12

# 5. 编译
make

# 6. 安装
sudo make install

# 7. 设置为系统服务
cd utils
sudo ./install_server.sh
```

##### 配置文件修改（两种方式通用）
```bash
# 编辑配置文件
sudo vim /etc/redis/redis.conf
```

关键配置项：
```
bind 0.0.0.0              # 允许远程访问（云服务器需要）
protected-mode no         # 关闭保护模式（生产环境建议开启并设置密码）
daemonize yes             # 后台运行
requirepass yourpassword  # 设置密码
port 6379                 # 默认端口

# 防火墙配置（云服务器）
sudo ufw allow 6379/tcp
```

##### 重启服务生效
```bash
sudo systemctl restart redis-server
```

---

#### 方案B：CentOS系统（供参考）

##### 环境准备
```bash
# 1. 更新系统包
sudo yum update -y

# 2. 安装编译工具
sudo yum install -y gcc make

# 3. 下载Redis（最新稳定版）
wget https://download.redis.io/releases/redis-7.0.12.tar.gz

# 4. 解压
tar -zxvf redis-7.0.12.tar.gz
cd redis-7.0.12

# 5. 编译
make

# 6. 安装
sudo make install
```

#### 配置文件
```bash
# 复制配置文件
sudo cp redis.conf /etc/redis.conf

# 修改配置
sudo vim /etc/redis.conf
```

关键配置项：
```
bind 0.0.0.0          # 允许远程访问
protected-mode no     # 关闭保护模式（生产环境需要密码）
daemonize yes         # 后台运行
requirepass yourpassword  # 设置密码
```

#### 启动验证
```bash
# 启动Redis
redis-server /etc/redis.conf

# 验证
redis-cli -a yourpassword ping
# 输出：PONG
```

### 1.3 Redis客户端工具

| 工具名称 | 特点 | 推荐度 |
|---------|------|--------|
| **RedisInsight** | Redis官方可视化工具，免费 | ⭐⭐⭐⭐⭐ |
| **Another Redis Desktop Manager** | 开源，界面美观 | ⭐⭐⭐⭐ |
| **命令行 redis-cli** | 简单直接 | ⭐⭐⭐ |

**RedisInsight连接配置**：
- Host: 云服务器IP
- Port: 6379
- Password: 配置的密码

### 1.4 redis-cli详细操作教程

#### 1.4.1 连接Redis
```bash
# 方式1：本地连接（默认端口6379，无密码）
redis-cli

# 方式2：指定主机连接
redis-cli -h 127.0.0.1

# 方式3：指定端口连接
redis-cli -p 6379

# 方式4：指定主机和端口连接
redis-cli -h 192.168.1.100 -p 6379

# 方式5：带密码连接
redis-cli -h 192.168.1.100 -p 6379 -a yourpassword

# 方式6：连接后再认证（更安全）
redis-cli -h 192.168.1.100 -p 6379
AUTH yourpassword

# 方式7：连接到特定数据库（默认0号库）
redis-cli -n 1  # 连接到1号库
```

**连接成功后测试：**
```bash
ping
# 输出：PONG
```

**退出redis-cli：**
```bash
exit
# 或者按 Ctrl+C
```

---

#### 1.4.2 字符串(String)操作
```bash
# 设置键值
SET name "张三"

# 获取键值
GET name

# 设置键值并设置过期时间（秒）
SETEX token 3600 "abc123"

# 只在键不存在时设置
SETNX name "李四"

# 设置多个键值
MSET name "张三" age 20 city "北京"

# 获取多个键值
MGET name age city

# 自增操作
SET count 0
INCR count       # 加1
INCRBY count 5   # 加5

# 自减操作
DECR count       # 减1
DECRBY count 3   # 减3

# 追加内容
APPEND name "先生"

# 获取字符串长度
STRLEN name
```

---

#### 1.4.3 哈希(Hash)操作
```bash
# 设置单个字段
HSET user:1 name "张三"

# 设置多个字段
HSET user:1 name "张三" age 18 city "北京"

# 获取单个字段
HGET user:1 name

# 获取所有字段
HGETALL user:1

# 获取所有字段名
HKEYS user:1

# 获取所有字段值
HVALS user:1

# 检查字段是否存在
HEXISTS user:1 name

# 删除字段
HDEL user:1 city

# 字段自增
HINCRBY user:1 age 1

# 获取字段数量
HLEN user:1
```

---

#### 1.4.4 列表(List)操作
```bash
# 从左侧添加元素
LPUSH list 1 2 3
# 结果：3 2 1

# 从右侧添加元素
RPUSH list 4 5
# 结果：3 2 1 4 5

# 获取列表长度
LLEN list

# 获取指定范围的元素
LRANGE list 0 -1    # 获取所有元素
LRANGE list 0 2     # 获取前3个元素

# 从左侧移除并返回元素
LPOP list

# 从右侧移除并返回元素
RPOP list

# 获取指定位置的元素
LINDEX list 0

# 在指定位置设置元素
LSET list 0 "new"

# 移除指定值的元素
LREM list 0 "value"

# 截取列表，保留指定范围
LTRIM list 0 4
```

---

#### 1.4.5 集合(Set)操作
```bash
# 添加元素
SADD set 1 2 3

# 获取所有元素
SMEMBERS set

# 获取元素数量
SCARD set

# 检查元素是否存在
SISMEMBER set 2

# 移除元素
SREM set 2

# 随机获取并移除一个元素
SPOP set

# 随机获取一个元素但不移除
SRANDMEMBER set

# 集合差集
SDIFF set1 set2

# 集合交集
SINTER set1 set2

# 集合并集
SUNION set1 set2
```

---

#### 1.4.6 有序集合(Sorted Set)操作
```bash
# 添加元素
ZADD zset 10 "apple"
ZADD zset 20 "banana"
ZADD zset 30 "orange"

# 获取指定范围的元素（按分数排序）
ZRANGE zset 0 -1

# 获取指定范围的元素和分数
ZRANGE zset 0 -1 WITHSCORES

# 获取指定分数范围的元素
ZRANGEBYSCORE zset 10 25

# 获取元素数量
ZCARD zset

# 获取元素分数
ZSCORE zset "apple"

# 增加元素分数
ZINCRBY zset 5 "apple"

# 移除元素
ZREM zset "apple"

# 获取元素排名（从小到大）
ZRANK zset "banana"

# 获取元素排名（从大到小）
ZREVRANK zset "banana"
```

---

#### 1.4.7 键(Key)操作
```bash
# 查看所有键
KEYS *

# 查看匹配模式的键
KEYS user:*

# 检查键是否存在
EXISTS key

# 查看键类型
TYPE key

# 删除键
DEL key

# 删除多个键
DEL key1 key2 key3

# 设置键的过期时间（秒）
EXPIRE key 60

# 设置键的过期时间（毫秒）
PEXPIRE key 60000

# 设置键在指定时间戳过期
EXPIREAT key 1717000000

# 查看键剩余过期时间（秒）
TTL key

# 查看键剩余过期时间（毫秒）
PTTL key

# 移除键的过期时间
PERSIST key

# 重命名键
RENAME oldkey newkey

# 移动键到另一个数据库
MOVE key 1
```

---

#### 1.4.8 数据库操作
```bash
# 查看当前数据库键数量
DBSIZE

# 切换到指定数据库（0-15）
SELECT 1

# 清空当前数据库
FLUSHDB

# 清空所有数据库
FLUSHALL

# 查看数据库信息
INFO
```

---

#### 1.4.9 持久化操作
```bash
# 后台保存RDB快照
BGSAVE

# 前台保存RDB快照（会阻塞）
SAVE

# 查看上次保存时间
LASTSAVE

# 手动重写AOF文件
BGREWRITEAOF

# 查看信息
INFO persistence
```

---

#### 1.4.10 常用实用命令
```bash
# 查看服务器信息
INFO

# 查看内存使用情况
INFO memory

# 查看统计信息
INFO stats

# 查看客户端连接信息
CLIENT LIST

# 查看慢查询日志
SLOWLOG GET

# 查看配置信息
CONFIG GET *

# 获取配置项
CONFIG GET maxmemory

# 设置配置项（运行时生效）
CONFIG SET maxmemory 1gb

# 监控实时命令（调试用）
MONITOR
```

---

#### 1.4.11 交互式模式操作技巧
```bash
# 在redis-cli中设置输出格式为raw（不包含类型信息）
redis-cli --raw

# 批量执行命令
redis-cli --scan --pattern "user:*"

# 执行脚本
redis-cli EVAL "return 'hello'" 0

# 管道方式执行多个命令
echo -e "SET key1 val1\nSET key2 val2" | redis-cli
```

---

#### 1.4.12 项目中常用操作示例

```bash
# 清空缓存（慎用）
FLUSHDB

# 查看部门缓存
KEYS dept:*

# 查看员工缓存
KEYS emp:*

# 删除部门缓存（重新加载时用）
DEL "dept::all"

# 查看某个部门详情
GET "dept::1"

# 查看缓存键的过期时间
TTL "dept::all"
```

---

### 1.5 数据持久化配置

#### RDB（快照）方式
```
save 900 1      # 900秒内至少1次写入
save 300 10     # 300秒内至少10次写入
save 60 10000   # 60秒内至少10000次写入
dbfilename dump.rdb
dir /var/lib/redis
```

#### AOF（追加文件）方式
```
appendonly yes          # 开启AOF
appendfsync everysec    # 每秒同步一次
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
```

---

## 🎯 二、MQ使用规划

### 2.1 MQ产品选择

| 产品 | 优势 | 劣势 | 推荐场景 |
|-----|------|------|---------|
| **RabbitMQ** | 功能完整、管理界面友好、文档丰富 | 内存占用较高 | **本地开发、中小型项目** ⭐ |
| **ActiveMQ** | 成熟稳定、JMS支持好 | 性能稍低 | 企业级遗留项目 |
| **RocketMQ** | 高性能、高可用 | 部署稍复杂 | 大型生产项目 |

**推荐选择**：RabbitMQ
- ✅ 部署简单
- ✅ 管理界面友好
- ✅ Spring Boot集成完善
- ✅ Windows支持好

### 2.2 RabbitMQ安装

#### 方案A：Ubuntu系统安装（推荐）

##### 步骤1：安装Erlang
```bash
# 更新包列表
sudo apt update

# 安装Erlang
sudo apt install -y erlang
```

##### 步骤2：添加RabbitMQ官方仓库
```bash
# 导入仓库签名密钥
curl -fsSL https://github.com/rabbitmq/signing-keys/releases/download/3.0/rabbitmq-release-signing-key.asc | sudo gpg --dearmor -o /usr/share/keyrings/rabbitmq-archive-keyring.gpg

# 添加仓库
echo "deb [signed-by=/usr/share/keyrings/rabbitmq-archive-keyring.gpg] https://ppa.launchpadcontent.net/rabbitmq/rabbitmq-server/ubuntu $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/rabbitmq.list
```

##### 步骤3：安装RabbitMQ
```bash
# 更新包列表
sudo apt update

# 安装RabbitMQ
sudo apt install -y rabbitmq-server

# 启动服务
sudo systemctl start rabbitmq-server

# 设置开机自启
sudo systemctl enable rabbitmq-server

# 检查状态
sudo systemctl status rabbitmq-server
```

##### 步骤4：启用管理插件
```bash
# 启用管理插件
sudo rabbitmq-plugins enable rabbitmq_management

# 重启服务
sudo systemctl restart rabbitmq-server
```

##### 步骤5：验证
- 管理界面：http://localhost:15672
- 默认账号：guest / guest
- 服务端口：5672

##### 防火墙配置（云服务器需要）
```bash
sudo ufw allow 5672/tcp
sudo ufw allow 15672/tcp
```

---

#### 方案B：Windows系统安装

##### 步骤1：安装Erlang
1. 下载：https://www.erlang.org/downloads
2. 选择Windows版本安装
3. 配置环境变量：`ERLANG_HOME`

##### 步骤2：安装RabbitMQ
1. 下载：https://www.rabbitmq.com/download.html
2. 选择Windows版本安装
3. 启动服务
```cmd
# 安装管理插件
rabbitmq-plugins enable rabbitmq_management

# 启动服务
rabbitmq-server start
```

##### 步骤3：验证
- 管理界面：http://localhost:15672
- 默认账号：guest / guest
- 服务端口：5672

### 2.3 网页管理界面操作

#### 主要功能：
1. **Exchanges（交换机）** - 查看/创建交换机
2. **Queues（队列）** - 查看/创建队列
3. **Connections（连接）** - 查看连接状态
4. **Overview（概览）** - 查看系统信息

#### 基本操作：
- 创建队列：Queues → Add queue
- 创建交换机：Exchanges → Add exchange
- 绑定队列：Queues → Bindings

### 2.4 消息生产者/消费者代码示例

#### 生产者（已实现）
```java
@Component
public class MQUtils {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendOperateLog(Object log) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.OPERATE_LOG_EXCHANGE,
            RabbitMQConfig.OPERATE_LOG_ROUTING_KEY,
            log
        );
    }
}
```

#### 消费者（已实现）
```java
@Component
public class OperateLogListener {
    @RabbitListener(queues = RabbitMQConfig.OPERATE_LOG_QUEUE)
    public void handleOperateLog(OperateLog log, Channel channel, 
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            // 处理消息
            operateLogService.insert(log);
            channel.basicAck(tag, false); // 确认
        } catch (Exception e) {
            channel.basicNack(tag, false, false); // 拒绝
        }
    }
}
```

---

## 🎯 三、整合使用建议

### 3.1 典型应用场景

| 场景 | Redis | MQ | 说明 |
|-----|-------|----|------|
| 缓存热点数据 | ✅ | ❌ | 部门列表、员工信息 |
| 异步任务处理 | ❌ | ✅ | 发送邮件、操作日志 |
| 分布式锁 | ✅ | ❌ | 防止重复提交 |
| 消息削峰 | ❌ | ✅ | 高并发请求处理 |
| 数据同步 | ✅ | ✅ | 缓存更新+消息通知 |

### 3.2 当前项目中的配合方式

```
用户请求 → Controller 
            ↓
            ├─→ Service → Redis（查询）
            │      ↓
            │   Mapper → DB
            │      ↓
            └─→ AOP → MQ（异步保存日志）
                   ↓
               Listener → DB
```

### 3.3 虚拟机连接配置指南

#### 3.3.1 获取虚拟机IP地址

**在Ubuntu虚拟机中执行：**
```bash
# 方式1：使用ip命令（推荐）
ip addr show

# 方式2：使用ifconfig（需要安装net-tools）
ifconfig
```

**查找类似信息：**
```
inet 192.168.1.100/24 brd 192.168.1.255 scope global dynamic
```
记录这个IP地址（例如：192.168.1.100）

---

#### 3.3.2 配置虚拟机网络

确保虚拟机网络模式为：
- **桥接模式(Bridged)** - 推荐，虚拟机与主机同网段
- **NAT模式** - 需要端口转发

**验证网络连通：**
```bash
# 在主机上ping虚拟机（Windows命令提示符）
ping 192.168.1.100

# 或者在虚拟机上ping主机
ping 您主机的IP地址
```

---

#### 3.3.3 配置Spring Boot连接虚拟机

项目已提供虚拟机专用配置文件 `application-vm.yml`

**步骤1：修改虚拟机IP地址**
编辑 `src/main/resources/application-vm.yml`，将：
```yaml
host: 192.168.1.100  # 改为您的虚拟机实际IP
```

**步骤2：启用虚拟机配置**

**方式1：修改主配置文件`application.yml`，添加：**
```yaml
spring:
  profiles:
    active: vm
```

**方式2：启动时指定配置文件**
```bash
# Maven启动
mvn spring-boot:run -Dspring-boot.run.profiles=vm

# 或者编译后
java -jar xxx.jar --spring.profiles.active=vm
```

**方式3：IDE运行时添加VM选项**
在运行配置中添加：
```
-Dspring.profiles.active=vm
```

---

#### 3.3.4 完整的虚拟机配置示例

```yaml
# application-vm.yml
spring:
  # Redis配置 - 虚拟机
  redis:
    host: 192.168.1.100        # 虚拟机IP
    port: 6379
    password: 123456            # 您设置的密码
    database: 0
    timeout: 10000ms
    lettuce:
      pool:
        max-active: 20
        max-wait: -1ms
        max-idle: 10
        min-idle: 5
  
  # RabbitMQ配置 - 虚拟机
  rabbitmq:
    host: 192.168.1.100        # 虚拟机IP
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

---

#### 3.3.5 虚拟机防火墙配置

**在Ubuntu虚拟机上开放端口：**

```bash
# 开放Redis端口
sudo ufw allow 6379/tcp

# 开放RabbitMQ端口
sudo ufw allow 5672/tcp
sudo ufw allow 15672/tcp  # 管理界面

# 查看防火墙状态
sudo ufw status

# 如果防火墙未开启，先启用
sudo ufw enable
```

**如果使用云服务器，还需要配置安全组：**
- 入站规则：允许TCP端口 6379, 5672, 15672
- 来源：您的主机IP，或 0.0.0.0/0（不安全，仅限开发）

---

#### 3.3.6 测试连接

**测试Redis连接：**
```bash
# 在主机上使用redis-cli（如果已安装）
redis-cli -h 192.168.1.100 -p 6379 -a 123456
ping
# 输出：PONG

# 或者使用telnet
telnet 192.168.1.100 6379
```

**测试RabbitMQ连接：**
```bash
# 访问管理界面（浏览器）
http://192.168.1.100:15672
# 账号：guest
# 密码：guest
```

---

#### 3.3.7 常见问题排查

| 问题 | 可能原因 | 解决方案 |
|------|---------|---------|
| 连接超时 | 防火墙拦截 | 检查防火墙和安全组 |
| 连接拒绝 | 服务未启动 | 检查Redis/RabbitMQ服务状态 |
| 认证失败 | 密码错误 | 检查配置的密码 |
| 无法访问 | IP地址错误 | 确认虚拟机IP是否正确 |
| NAT模式无法访问 | 端口未转发 | 配置虚拟机端口转发 |

**检查服务状态（虚拟机中）：**
```bash
# Redis
sudo systemctl status redis-server

# RabbitMQ
sudo systemctl status rabbitmq-server
```

---

### 3.4 本地开发环境网络配置（原配置，仅做参考）

#### 连接远程Redis
在 `application.yml` 中配置：
```yaml
spring:
  data:
    redis:
      host: your-remote-ip
      port: 6379
      password: your-password
      timeout: 5000
```

#### 本地连接RabbitMQ
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

### 3.4 故障排查建议

| 问题 | 排查步骤 |
|-----|---------|
| 连接Redis失败 | 检查防火墙/安全组、bind配置、密码 |
| Redis内存不足 | 配置maxmemory-policy为allkeys-lru |
| MQ消息积压 | 增加消费者、优化处理逻辑 |
| 消息丢失 | 开启持久化、确认机制 |

---

## 🎯 四、验证标准

### 4.1 Redis验证步骤

#### 步骤1：连接验证
```bash
# 使用redis-cli连接
redis-cli -h your-host -p 6379 -a your-password
# 测试
ping
# 预期结果：PONG
```

#### 步骤2：基本操作验证
```bash
# 设置数据
SET test:key hello
# 获取数据
GET test:key
# 预期结果："hello"

# 设置过期时间
SETEX test:expire 60 "60秒过期"
# 查看剩余时间
TTL test:expire
# 预期结果：整数（秒数）

# 删除数据
DEL test:key
GET test:key
# 预期结果：(nil)
```

#### 步骤3：持久化验证
```bash
# 1. 设置数据
SET persist:test 123

# 2. 执行保存
BGSAVE

# 3. 重启Redis
redis-cli shutdown
redis-server /etc/redis.conf

# 4. 验证数据还在
GET persist:test
# 预期结果："123"
```

### 4.2 MQ验证步骤

#### 步骤1：连接验证
1. 访问 http://localhost:15672
2. 使用 guest/guest 登录
3. 能看到概览页面

#### 步骤2：消息发送验证
1. 执行系统操作（如：新增部门）
2. 查看RabbitMQ管理界面
3. 预期：Queues中消息被消费

#### 步骤3：消息确认验证
1. 查看OperateLogListener日志
2. 预期：看到"操作日志保存成功"
3. 查看数据库operate_log表
4. 预期：有新记录插入

### 4.3 完整环境测试流程

```
1. 启动Redis
   ↓
2. 启动RabbitMQ
   ↓
3. 启动Spring Boot应用
   ↓
4. 执行操作（新增部门）
   ↓
5. 验证：
   - Redis中缓存了部门列表
   - MQ中消息被消费
   - 数据库中操作日志已保存
```

---

## 📋 总结

本方案提供了：
- ✅ Redis多种部署方案及详细步骤
- ✅ RabbitMQ Windows安装配置
- ✅ 项目整合使用建议
- ✅ 完整的验证标准

下一步建议：
1. 选择适合的Redis部署方案
2. 按步骤部署配置
3. 进行验证测试
4. 根据需要调整配置
