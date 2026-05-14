# Ubuntu虚拟机 Redis + RabbitMQ 配置指南

## 📋 环境信息

- Ubuntu版本: 20.04 LTS / 22.04 LTS
- IP地址: 192.168.88.129
- 非root用户: 根据您的实际用户

---

## 🔧 一、基础环境准备

### 1. 更新系统包
```bash
sudo apt update && sudo apt upgrade -y
```

### 2. 安装必要工具
```bash
sudo apt install -y curl wget vim net-tools ufw
```

---

## 🚀 二、Redis安装与配置

### 1. 安装Redis
```bash
sudo apt install -y redis-server
```

### 2. 配置Redis

#### 2.1 备份原始配置
```bash
sudo cp /etc/redis/redis.conf /etc/redis/redis.conf.bak
```

#### 2.2 修改Redis配置
```bash
sudo vim /etc/redis/redis.conf
```

#### 2.3 主要配置项（按以下内容修改）：
```ini
# 绑定地址 - 允许远程访问
bind 0.0.0.0

# 保护模式关闭
protected-mode no

# 设置密码
requirepass 123456

# 持久化配置
save 900 1
save 300 10
save 60 10000

# AOF持久化
appendonly yes
appendfsync everysec

# 内存淘汰策略
maxmemory 2gb
maxmemory-policy allkeys-lru

# 端口
port 6379

# 守护进程模式
daemonize yes
```

### 3. 启动并设置开机自启
```bash
# 启动Redis
sudo systemctl start redis-server

# 设置开机自启
sudo systemctl enable redis-server

# 查看状态
sudo systemctl status redis-server
```

### 4. 测试Redis连接
```bash
# 本地测试
redis-cli -a 123456 ping

# 应该返回: PONG
```

### 5. 配置防火墙（UFW）
```bash
# 允许Redis端口
sudo ufw allow 6379/tcp

# 查看防火墙状态
sudo ufw status
```

---

## 🐰 三、RabbitMQ安装与配置

### 1. 安装依赖
```bash
sudo apt install -y erlang
```

### 2. 添加RabbitMQ仓库
```bash
curl -fsSL https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.deb.sh | sudo bash
```

### 3. 安装RabbitMQ
```bash
sudo apt install -y rabbitmq-server
```

### 4. 启用管理插件
```bash
sudo rabbitmq-plugins enable rabbitmq_management
```

### 5. 创建管理员用户
```bash
# 添加用户
sudo rabbitmqctl add_user admin 123456

# 设置管理员权限
sudo rabbitmqctl set_user_tags admin administrator

# 设置权限
sudo rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

### 6. 配置RabbitMQ
```bash
sudo vim /etc/rabbitmq/rabbitmq.conf
```

添加或修改以下内容：
```ini
listeners.tcp.default = 0.0.0.0:5672
management.listener.port = 15672
management.listener.ip = 0.0.0.0
```

### 7. 启动RabbitMQ
```bash
# 启动服务
sudo systemctl start rabbitmq-server

# 设置开机自启
sudo systemctl enable rabbitmq-server

# 查看状态
sudo systemctl status rabbitmq-server
```

### 8. 配置防火墙
```bash
# 允许RabbitMQ端口
sudo ufw allow 5672/tcp
sudo ufw allow 15672/tcp
```

### 9. 访问管理界面
在浏览器访问：`http://192.168.88.129:15672`
- 用户名: `admin`
- 密码: `123456`

---

## 📊 四、验证安装

### 1. 验证Redis
```bash
# 查看Redis信息
redis-cli -a 123456 info

# 测试写入
redis-cli -a 123456 set test_key "Hello Redis"
redis-cli -a 123456 get test_key
```

### 2. 验证RabbitMQ
```bash
# 查看RabbitMQ状态
sudo rabbitmqctl status

# 查看队列
sudo rabbitmqctl list_queues
```

---

## 🔄 五、常用管理命令

### Redis管理
```bash
# 启动/停止/重启
sudo systemctl start redis-server
sudo systemctl stop redis-server
sudo systemctl restart redis-server

# 查看日志
sudo tail -f /var/log/redis/redis-server.log

# 进入Redis命令行
redis-cli -a 123456
```

### RabbitMQ管理
```bash
# 启动/停止/重启
sudo systemctl start rabbitmq-server
sudo systemctl stop rabbitmq-server
sudo systemctl restart rabbitmq-server

# 查看日志
sudo tail -f /var/log/rabbitmq/rabbitmq-server.log

# 用户管理
sudo rabbitmqctl list_users
sudo rabbitmqctl change_password admin new_password
```

---

## 🔧 六、性能优化配置（可选）

### Redis优化
编辑 `/etc/redis/redis.conf`：
```ini
# 最大客户端连接数
maxclients 10000

# 客户端输出缓冲区限制
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit slave 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60

# 慢查询日志
slowlog-log-slower-than 10000
slowlog-max-len 128
```

### 系统优化
编辑 `/etc/sysctl.conf`：
```ini
# 文件描述符
fs.file-max = 1000000

# TCP优化
net.core.somaxconn = 65535
net.core.netdev_max_backlog = 65535
net.ipv4.tcp_max_syn_backlog = 65535
net.ipv4.tcp_tw_reuse = 1
net.ipv4.ip_local_port_range = 1024 65535

# 内存优化
vm.swappiness = 10
vm.overcommit_memory = 1
```

然后执行：
```bash
sudo sysctl -p
```

---

## ⚠️ 七、故障排查

### 检查端口是否开放
```bash
# 检查Redis
netstat -tlnp | grep 6379

# 检查RabbitMQ
netstat -tlnp | grep 5672
```

### 查看服务日志
```bash
# Redis日志
sudo journalctl -u redis-server -f

# RabbitMQ日志
sudo journalctl -u rabbitmq-server -f
```

### 从Windows测试连接
在Windows PowerShell中：
```powershell
# 测试Redis端口连通性
Test-NetConnection -ComputerName 192.168.88.129 -Port 6379

# 测试RabbitMQ端口连通性
Test-NetConnection -ComputerName 192.168.88.129 -Port 5672
```

---

## 📝 八、安全建议

1. **生产环境修改密码**：不要使用弱密码
2. **限制IP访问**：在Redis配置中使用特定IP代替0.0.0.0
3. **使用防火墙**：只允许应用服务器访问Redis和RabbitMQ
4. **开启TLS加密**：生产环境建议使用SSL/TLS

---

## ✅ 配置清单

安装完成后，请确认以下内容：

- [ ] Redis已安装并运行在 `6379` 端口
- [ ] Redis密码设置为 `123456`
- [ ] Redis允许远程访问
- [ ] RabbitMQ已安装并运行在 `5672` 端口
- [ ] RabbitMQ管理界面在 `15672` 端口可访问
- [ ] 防火墙已开放相关端口
- [ ] 从Windows可以ping通虚拟机
- [ ] 从Windows可以连接Redis和RabbitMQ
