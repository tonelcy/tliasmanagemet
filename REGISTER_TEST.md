# 注册功能测试文档

## 接口信息

- **接口路径**：`POST /register`
- **Content-Type**：`application/json`

## 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名（2-20个字符） |
| password | String | 是 | 密码（6-20个字符） |

## 响应数据格式

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "name": "testuser",
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00"
  }
}
```

## 测试用例

### 1. 正常注册成功

**请求**：
```json
{
  "username": "newuser01",
  "password": "123456"
}
```

**预期结果**：
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "newuser01",
    "name": "newuser01"
  }
}
```

---

### 2. 用户名为空

**请求**：
```json
{
  "username": "",
  "password": "123456"
}
```

**预期结果**：
```json
{
  "code": 0,
  "msg": "用户名不能为空"
}
```

---

### 3. 用户名长度不足2位

**请求**：
```json
{
  "username": "a",
  "password": "123456"
}
```

**预期结果**：
```json
{
  "code": 0,
  "msg": "用户名长度需在2-20个字符之间"
}
```

---

### 4. 密码为空

**请求**：
```json
{
  "username": "testuser",
  "password": ""
}
```

**预期结果**：
```json
{
  "code": 0,
  "msg": "密码不能为空"
}
```

---

### 5. 密码长度不足6位

**请求**：
```json
{
  "username": "testuser",
  "password": "12345"
}
```

**预期结果**：
```json
{
  "code": 0,
  "msg": "密码长度需在6-20个字符之间"
}
```

---

### 6. 用户名已存在

**前置条件**：用户 `existinguser` 已经注册过

**请求**：
```json
{
  "username": "existinguser",
  "password": "123456"
}
```

**预期结果**：
```json
{
  "code": 0,
  "msg": "用户名已存在"
}
```

---

### 7. 使用注册的用户登录

**前置条件**：用户 `testlogin` 已经注册，密码为 `123456`

**登录请求**：
```json
{
  "username": "testlogin",
  "password": "123456"
}
```

**预期结果**：登录成功，返回 JWT token

---

## 使用 curl 测试

```bash
# 正常注册
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "test001", "password": "123456"}'

# 用户名为空
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "", "password": "123456"}'

# 密码太短
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "test002", "password": "123"}'
```

## 安全特性

1. ✅ 密码使用 BCrypt 加密存储
2. ✅ 用户名唯一性校验
3. ✅ 参数长度校验
4. ✅ 防 SQL 注入（使用 MyBatis 参数化查询）
5. ✅ 全局异常处理
6. ✅ 登录时验证加密后的密码
