# RabbitMQ 管理界面完整使用指南

## 📋 目录
1. [快速登录](#快速登录)
2. [界面总览](#界面总览)
3. [主菜单详解](#主菜单详解)
4. [核心功能操作](#核心功能操作)
5. [实际使用场景](#实际使用场景)
6. [监控与故障排查](#监控与故障排查)

---

## 🔐 快速登录

### 登录信息
| 配置项 | 值 |
|-------|-----|
| **访问地址** | `http://192.168.88.129:15672` |
| **用户名** | `admin` |
| **密码** | `123456` |

### 登录步骤
1. 在浏览器中打开：`http://192.168.88.129:15672`
2. 输入用户名：`admin`
3. 输入密码：`123456`
4. 点击 **Login** 按钮

---

## 🖼️ 界面总览

### 首页概览（Overview）

登录成功后，您会看到以下内容：

```
┌─────────────────────────────────────────────────────────────┐
│  RabbitMQ Management  [admin]  [User:admin]  [Logout]         │
├─────────────────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Overview  Connections  Channels  Exchanges            │  │
│  │ Queues  Admin  [更多标签页]                            │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ RabbitMQ Overview                                      │  │
│  ├───────────────────────────────────────────────────────┤  │
│  │ • Totals                                              │  │
│  │   - Connections: 5                                    │  │
│  │   - Channels: 8                                      │  │
│  │   - Queues: 3                                        │  │
│  │   - Exchanges: 8                                     │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Node: rabbit@localhost (运行状态)                      │  │
│  ├───────────────────────────────────────────────────────┤  │
│  │ • Erlang Processes: 123                               │  │
│  │ • Memory: 256MB                                       │  │
│  │ • File Descriptors: 50/1024                            │  │
│  │ • Socket Descriptors: 10/8192                         │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Message Rates (消息速率图表)                            │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 主菜单详解

### 菜单导航栏位置

位于页面顶部，包含以下主要菜单：

| 菜单项 | 作用 |
|--------|------|
| **Overview** | 概览页面 - 查看系统整体状态 |
| **Connections** | 连接管理 - 查看和管理客户端连接 |
| **Channels** | 信道管理 - 查看和管理信道 |
| **Exchanges** | 交换机管理 - 创建、配置交换机 |
| **Queues** | 队列管理 - 创建、配置队列 |
| **Admin** | 管理员功能 - 用户、权限、策略等 |

---

## 1️⃣ Overview（概览页面）

### 主要功能区域

#### A. Totals（总览统计）
显示 RabbitMQ 核心统计：
- **Connections** - 当前活跃连接数
- **Channels** - 当前信道数
- **Queues** - 队列数量
- **Consumers** - 消费者数量
- **Messages** - 消息总数（待确认、待投递等）

#### B. Nodes（节点信息）
- **节点名称** - 例如 `rabbit@localhost`
- **运行状态** - 显示绿色（运行中）或红色（停止）
- **Erlang 进程数** - 运行的进程数量
- **内存使用** - 当前内存占用和限制
- **文件描述符** - 文件句柄使用情况

#### C. Message Rates（消息速率图表）
实时图表显示：
- **Publish** - 消息发布速率
- **Deliver** - 消息投递速率
- **Acknowledge** - 消息确认速率
- **Redeliver** - 消息重新投递速率

---

## 2️⃣ Connections（连接管理）

### 页面介绍
显示所有与 RabbitMQ 建立的 TCP 连接。

### 表格列说明

| 列名 | 含义 |
|------|------|
| **User** | 使用的用户名 |
| **Virtual Host** | 虚拟主机 |
| **Client properties** | 客户端信息（应用名称、版本等） |
| **Protocol** | 使用的协议（AMQP 0-9-1 等） |
| **Channels** | 该连接上的信道数 |
| **State** | 连接状态（running/flow 等） |
| **SSL** | 是否使用 SSL 加密 |

### 可执行操作

#### 断开连接
1. 找到要断开的连接
2. 点击连接右侧的 **Close Connection** 按钮
3. 确认操作

#### 查看连接详情
1. 点击连接的 **User** 名称
2. 查看详细信息和统计数据

---

## 3️⃣ Channels（信道管理）

### 页面介绍
显示所有连接上创建的信道（Channel）。

### 表格列说明

| 列名 | 含义 |
|------|------|
| **Channel** | 信道编号 |
| **User** | 所属用户 |
| **Mode** | 工作模式（confirm、tx 等） |
| **Prefetch** | 预取数量（QoS 设置） |
| **Unacknowledged** | 未确认的消息数 |
| **Unconfirmed** | 未确认的发布数 |
| **Uncommitted** | 未提交的事务数 |
| **State** | 状态 |

### 操作按钮
- **Force Close** - 强制关闭信道

---

## 4️⃣ Exchanges（交换机管理）⭐

### 页面介绍
交换机是消息的路由中心，负责接收消息并路由到队列。

### 页面布局

```
┌─────────────────────────────────────────────────────────┐
│  Add a new exchange  │  [All]  [AMQP default]  [topic] │
├─────────────────────────────────────────────────────────┤
│  Name          Type        Features    Message rates     │
│  ──────────────────────────────────────────────────────  │
│  (direct)      Direct      D           0/s              │
│  (topic)       Topic       D           0/s              │
│  (fanout)      Fanout      D           0/s              │
│  (headers)     Headers     D           0/s              │
│  amq.direct    Direct      D           0/s              │
│  my.exchange   Topic       -           10/s             │
└─────────────────────────────────────────────────────────┘
```

### 交换机类型

| 类型 | 说明 | 路由规则 |
|------|------|---------|
| **Direct** | 直连交换机 | 精确匹配 routing key |
| **Topic** | 主题交换机 | 通配符匹配（*、#） |
| **Fanout** | 广播交换机 | 忽略 routing key，发给所有绑定队列 |
| **Headers** | 头交换机 | 根据消息头属性匹配 |

### 核心操作

#### A. 创建交换机

1. 点击 **Add a new exchange** 展开表单
2. 填写以下信息：

| 字段 | 说明 | 示例 |
|------|------|------|
| **Name** | 交换机名称 | `my.application.exchange` |
| **Type** | 类型（direct/topic/fanout/headers） | `topic` |
| **Durability** | 持久性（Durable/Transient） | `Durable` |
| **Auto delete** | 自动删除（Yes/No） | `No` |
| **Internal** | 内部使用（Yes/No） | `No` |
| **Arguments** | 其他参数（可选） | |

3. 点击 **Add exchange** 按钮

#### B. 删除交换机
1. 在列表中找到要删除的交换机
2. 点击交换机名称进入详情页
3. 点击 **Delete this Exchange** 按钮
4. 确认删除

#### C. 发布消息
1. 进入交换机详情页
2. 找到 **Publish message** 区域
3. 填写：
   - **Routing key** - 路由键
   - **Payload** - 消息内容（JSON 等）
   - **Headers** - 消息头（可选）
4. 点击 **Publish message** 按钮

---

## 5️⃣ Queues（队列管理）⭐⭐

### 页面介绍
队列是存储消息的地方，消费者从队列获取消息。

### 页面布局

```
┌─────────────────────────────────────────────────────────┐
│  Add a new queue  │  [All]  [/]  [vhost1]  [vhost2]     │
├─────────────────────────────────────────────────────────┤
│  Virtual Host  Name          Features    Messages       │
│  ─────────────────────────────────────────────────────  │
│  /             queue1        D           100            │
│  /             queue2        D           0              │
│  /vhost1       order.queue   D           50             │
└─────────────────────────────────────────────────────────┘
```

### 核心操作

#### A. 创建队列

1. 点击 **Add a new queue** 展开表单
2. 填写以下信息：

| 字段 | 说明 | 示例 |
|------|------|------|
| **Virtual host** | 虚拟主机 | `/` |
| **Name** | 队列名称 | `user.register.queue` |
| **Durability** | 持久性（Durable/Transient） | `Durable` |
| **Auto delete** | 自动删除（Yes/No） | `No` |
| **Exclusive** | 排他（Yes/No） | `No` |
| **Arguments** | 参数（TTL、死信等） | |

3. 点击 **Add queue** 按钮

#### B. 队列参数说明

| 参数 | 说明 | 示例值 |
|------|------|--------|
| **x-message-ttl** | 消息生存时间（毫秒） | `60000`（1分钟） |
| **x-expires** | 队列自动删除时间 | `1800000`（30分钟） |
| **x-max-length** | 最大消息数 | `10000` |
| **x-dead-letter-exchange** | 死信交换机 | `dlx.exchange` |
| **x-dead-letter-routing-key** | 死信路由键 | `dlx` |
| **x-single-active-consumer** | 单活跃消费者 | `true` |

#### C. 队列操作

1. **获取消息** - 从队列取出查看（不确认）
2. **发布消息** - 直接向队列发送消息
3. **移动消息** - 将消息移动到另一个队列
4. **删除消息** - 清空队列中的消息
5. **删除队列** - 删除整个队列

#### D. 绑定队列到交换机

这是最重要的操作，消息通过交换机路由到队列：

1. 进入队列详情页
2. 找到 **Bindings** 区域
3. 点击 **Add binding to this queue**
4. 填写：
   - **From exchange** - 源交换机名称
   - **Routing key** - 路由键（与交换机类型匹配）
5. 点击 **Bind** 按钮

---

## 6️⃣ Admin（管理员功能）⭐⭐

### 用户管理（Users）

#### 创建用户
1. 点击 **Add a user**
2. 填写：
   - **Username** - 用户名
   - **Password** - 密码
   - **Tags** - 用户标签（管理员用 administrator）
3. 点击 **Add user**

#### 用户标签说明

| 标签 | 权限 |
|------|------|
| **administrator** | 超级管理员 - 全部权限 |
| **monitoring** | 监控 - 只读管理API权限 |
| **policymaker** | 策略 - 管理策略和参数 |
| **management** | 管理 - 访问管理界面 |
| **none** | 无标签 - 只能通过AMQP使用 |

#### 设置用户权限
1. 点击用户名进入详情页
2. 在 **Permissions** 区域选择虚拟主机
3. 配置权限：
   - **Configure regex** - 配置权限（队列、交换机创建等）
   - **Write regex** - 写入权限（发布消息）
   - **Read regex** - 读取权限（消费消息）
4. 点击 **Set permission**

---

### 虚拟主机管理（Virtual Hosts）

#### 创建虚拟主机
1. 点击 **Add a new virtual host**
2. 输入名称（例如：`/myapp`）
3. 点击 **Add virtual host**

#### 设置虚拟主机权限
1. 点击虚拟主机名称
2. 在 **Permissions** 中选择用户
3. 设置权限（同上）

---

### 策略管理（Policies）

策略用于批量配置队列和交换机。

#### 创建策略
1. 点击 **Add a policy**
2. 填写：
   - **Virtual host** - 虚拟主机
   - **Name** - 策略名称
   - **Pattern** - 匹配模式（正则表达式）
   - **Apply to** - 应用到（queues/exchanges/all）
   - **Definition** - 策略定义（JSON）
   - **Priority** - 优先级
3. 点击 **Add policy**

**示例策略：**
```
Name: mirror-all
Pattern: .*
Apply to: Queues and exchanges
Definition: ha-mode=all
```

---

## 🔧 核心功能操作

### 场景一：创建完整消息链路

假设我们要创建一个用户注册的消息处理流程。

#### 步骤 1：创建交换机
1. Exchanges → Add a new exchange
2. Name: `user.events`
3. Type: `topic`
4. Durability: `Durable`
5. 点击 **Add exchange**

#### 步骤 2：创建队列
1. Queues → Add a new queue
2. Name: `user.register.notification`
3. Durability: `Durable`
4. 点击 **Add queue**

#### 步骤 3：绑定队列到交换机
1. 进入队列详情页
2. Bindings → Add binding
3. From exchange: `user.events`
4. Routing key: `user.registered`
5. 点击 **Bind**

#### 步骤 4：测试发送消息
1. 进入 `user.events` 交换机
2. Publish message:
   - Routing key: `user.registered`
   - Payload: `{"userId":123,"email":"test@example.com"}`
3. 点击 **Publish message**

#### 步骤 5：验证消息
1. 进入 `user.register.notification` 队列
2. 点击 **Get messages**
3. Messages: `1`
4. 点击 **Get Message(s)**
5. 查看消息内容

---

### 场景二：配置死信队列（DLQ）

用于处理无法正常消费的消息。

#### 步骤 1：创建死信交换机
1. Exchanges → Add a new exchange
2. Name: `user.events.dlx`
3. Type: `direct`
4. Durability: `Durable`

#### 步骤 2：创建死信队列
1. Queues → Add a new queue
2. Name: `user.register.dlq`
3. Durability: `Durable`

#### 步骤 3：绑定死信队列
1. 绑定到 `user.events.dlx`
2. Routing key: `dlq`

#### 步骤 4：创建业务队列（带死信配置）
1. Queues → Add a new queue
2. Name: `user.register.notification`
3. Durability: `Durable`
4. Arguments:
   - x-dead-letter-exchange: `user.events.dlx`
   - x-dead-letter-routing-key: `dlq`

---

### 场景三：查看和调试消息

#### 查看队列状态
1. Queues 页面
2. 查看以下列：
   - **Ready** - 待消费的消息数
   - **Unacked** - 已发送但未确认的消息数
   - **Total** - 总消息数
   - **Consumers** - 活跃的消费者数

#### 获取消息查看
1. 进入队列详情
2. 点击 **Get messages**
3. 选择消息数量和方式（Ack/Reject/Requeue）
4. 点击 **Get Message(s)**

#### 移动消息
1. 进入队列详情
2. 点击 **Move messages**
3. 选择目标队列
4. 点击 **Move messages**

---

## 📊 监控与故障排查

### 1. 查看系统状态

#### Overview 页面关键指标
| 指标 | 正常情况 | 警告情况 |
|------|---------|---------|
| **Message rates** | 平稳波动 | 突然下降或飙升 |
| **Memory usage** | 稳定 | 持续增长接近限制 |
| **File descriptors** | 充足 | 接近上限 |

#### Connections 页面
- 检查连接数是否异常
- 查看客户端信息识别问题来源

#### Queues 页面
- 检查 Ready 消息是否堆积
- 检查 Unacked 消息是否过多
- 查看消费者是否正常连接

---

### 2. 常用管理命令

除了界面，您也可以使用命令行：

#### 查看队列状态
```bash
sudo rabbitmqctl list_queues
```

#### 查看连接
```bash
sudo rabbitmqctl list_connections
```

#### 查看消费者
```bash
sudo rabbitmqctl list_consumers
```

#### 关闭连接
```bash
sudo rabbitmqctl close_connection <connection_name> "manual close"
```

#### 清除策略
```bash
sudo rabbitmqctl clear_policy <policy_name>
```

---

### 3. 常见问题排查

#### 问题：消息堆积在队列
**可能原因：**
1. 消费者程序未运行
2. 消费者处理速度慢
3. 消费者没有正确确认消息

**排查：**
1. 检查 Consumers 列是否有消费者
2. 检查 Unacked 列是否有值
3. 查看消费者日志

---

#### 问题：消息无法路由
**现象：** 消息发送了但队列没有收到

**可能原因：**
1. 交换机没有绑定队列
2. Routing key 不匹配
3. 消息被交换机拒绝

**排查：**
1. 检查交换机的 Bindings
2. 检查发送时的 Routing key
3. 查看是否有 Return 消息

---

## 🎯 常用操作速查表

| 操作 | 菜单路径 | 说明 |
|------|---------|------|
| 查看队列消息数 | Queues → 列表 | 看 Ready 列 |
| 创建交换机 | Exchanges → Add exchange | |
| 创建队列 | Queues → Add queue | |
| 绑定队列交换机 | 队列详情 → Bindings | |
| 测试发送消息 | 交换机详情 → Publish | |
| 查看消息内容 | 队列详情 → Get messages | |
| 创建用户 | Admin → Users → Add user | |
| 配置权限 | Admin → 用户 → Set permission | |
| 查看连接数 | Overview/Connections | |
| 查看内存使用 | Overview → Node | |

---

## 📝 最佳实践建议

### 命名规范
- 交换机：`业务名.事件类型`
- 队列：`业务名.动作.队列`
- 路由键：`业务.实体.动作`

### 安全建议
1. 定期更换密码
2. 按最小权限分配
3. 使用不同的虚拟主机隔离环境

### 性能建议
1. 避免过多交换机和队列
2. 合理设置消息 TTL
3. 监控并及时报警
4. 使用死信队列处理异常

---

## 🚀 快速上手 checklist

- [ ] 成功登录管理界面
- [ ] 查看 Overview 页面了解系统状态
- [ ] 创建一个测试交换机
- [ ] 创建一个测试队列
- [ ] 绑定它们
- [ ] 发布并消费一条测试消息
- [ ] 查看监控图表确认正常

---

## 📚 更多资料

- [RabbitMQ 官方文档](https://www.rabbitmq.com/docs)
- [RabbitMQ 教程](https://www.rabbitmq.com/tutorials)
