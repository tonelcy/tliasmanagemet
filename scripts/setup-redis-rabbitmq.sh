#!/bin/bash

# Ubuntu Redis + RabbitMQ 一键安装脚本
# 使用方法: sudo bash setup-redis-rabbitmq.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置
REDIS_PASSWORD="123456"
RABBITMQ_USER="admin"
RABBITMQ_PASSWORD="123456"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Ubuntu Redis + RabbitMQ 安装脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 更新系统
echo -e "${YELLOW}[1/6] 更新系统包...${NC}"
sudo apt update && sudo apt upgrade -y

# 安装基础工具
echo -e "${YELLOW}[2/6] 安装必要工具...${NC}"
sudo apt install -y curl wget vim net-tools ufw

# 安装 Redis
echo -e "${YELLOW}[3/6] 安装 Redis...${NC}"
sudo apt install -y redis-server

# 备份 Redis 配置
sudo cp /etc/redis/redis.conf /etc/redis/redis.conf.bak

# 配置 Redis
echo -e "${YELLOW}配置 Redis...${NC}"
sudo tee /etc/redis/redis.conf > /dev/null <<EOF
bind 0.0.0.0
protected-mode no
requirepass ${REDIS_PASSWORD}
save 900 1
save 300 10
save 60 10000
appendonly yes
appendfsync everysec
maxmemory 2gb
maxmemory-policy allkeys-lru
port 6379
daemonize yes
EOF

# 重启 Redis
sudo systemctl restart redis-server
sudo systemctl enable redis-server

# 安装 Erlang (RabbitMQ 依赖)
echo -e "${YELLOW}[4/6] 安装 Erlang...${NC}"
sudo apt install -y erlang

# 添加 RabbitMQ 仓库
echo -e "${YELLOW}[5/6] 安装 RabbitMQ...${NC}"
curl -fsSL https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.deb.sh | sudo bash
sudo apt install -y rabbitmq-server

# 启用 RabbitMQ 管理插件
sudo rabbitmq-plugins enable rabbitmq_management

# 创建 RabbitMQ 用户
sudo rabbitmqctl add_user ${RABBITMQ_USER} ${RABBITMQ_PASSWORD} || true
sudo rabbitmqctl set_user_tags ${RABBITMQ_USER} administrator
sudo rabbitmqctl set_permissions -p / ${RABBITMQ_USER} ".*" ".*" ".*"

# 配置 RabbitMQ
sudo tee /etc/rabbitmq/rabbitmq.conf > /dev/null <<EOF
listeners.tcp.default = 0.0.0.0:5672
management.listener.port = 15672
management.listener.ip = 0.0.0.0
EOF

# 重启 RabbitMQ
sudo systemctl restart rabbitmq-server
sudo systemctl enable rabbitmq-server

# 配置防火墙
echo -e "${YELLOW}[6/6] 配置防火墙...${NC}"
sudo ufw allow 6379/tcp
sudo ufw allow 5672/tcp
sudo ufw allow 15672/tcp

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}安装完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Redis配置："
echo "  地址: 192.168.88.129:6379"
echo "  密码: ${REDIS_PASSWORD}"
echo ""
echo "RabbitMQ配置："
echo "  地址: 192.168.88.129:5672"
echo "  管理界面: http://192.168.88.129:15672"
echo "  用户名: ${RABBITMQ_USER}"
echo "  密码: ${RABBITMQ_PASSWORD}"
echo ""
echo "服务状态："
echo -e "  Redis: $(sudo systemctl is-active redis-server)"
echo -e "  RabbitMQ: $(sudo systemctl is-active rabbitmq-server)"
echo ""
echo -e "${YELLOW}测试连接：${NC}"
echo "  redis-cli -a ${REDIS_PASSWORD} ping"
echo ""
