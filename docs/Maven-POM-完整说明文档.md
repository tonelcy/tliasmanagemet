# Maven POM (Project Object Model) 完整说明文档

## 📋 目录
1. [项目元数据](#项目元数据)
2. [Properties 属性配置](#properties-属性配置)
3. [Dependencies 依赖管理](#dependencies-依赖管理)
4. [DependencyManagement 依赖版本管理](#dependencyManagement-依赖版本管理)
5. [Build 构建配置](#build-构建配置)
6. [最佳实践建议](#最佳实践建议)

---

## 📁 1. 项目元数据

### 1.1 XML 声明与命名空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
```

| 元素 | 说明 |
|------|------|
| `<?xml version="1.0" encoding="UTF-8"?>` | XML 文件声明，指定版本和编码 |
| `xmlns="http://maven.apache.org/POM/4.0.0"` | Maven 4.0 命名空间 |
| `xsi:schemaLocation` | XML Schema 验证位置，确保 POM 格式正确 |

### 1.2 核心坐标信息

```xml
<modelVersion>4.0.0</modelVersion>
<groupId>com.lcy</groupId>
<artifactId>tlias-web-management</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

#### 详解：

| 元素 | 值 | 作用 | 示例 |
|------|----|------|------|
| **modelVersion** | `4.0.0` | POM 模型版本，固定 4.0.0 | - |
| **groupId** | `com.lcy` | 组织/公司标识，通常是域名反转 | `com.alibaba` |
| **artifactId** | `tlias-web-management` | 项目/模块标识，描述用途 | `spring-boot-starter-web` |
| **version** | `0.0.1-SNAPSHOT` | 版本号，`-SNAPSHOT` 表示开发版本 | `1.0.0-RELEASE` |

**项目坐标完整示例：**
```xml
<!-- 这三个组成 Maven 坐标 -->
<groupId>com.lcy</groupId>
<artifactId>tlias-web-management</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

**版本号规范建议：**
```
主版本.次版本.修订版[-标记]
示例：
  1.0.0-SNAPSHOT (开发中)
  1.0.0-RC1 (发布候选)
  1.0.0-RELEASE (正式发布)
  2.1.3 (稳定版)
```

### 1.3 项目描述信息

```xml
<name>tlias-web-management</name>
<description>tlias-web-management</description>
```

| 元素 | 作用 |
|------|------|
| `name` | 项目友好名称，用于文档展示 |
| `description` | 项目描述，说明项目用途 |

---

## ⚙️ 2. Properties 属性配置

```xml
<properties>
    <java.version>1.8</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <spring-boot.version>2.6.13</spring-boot.version>
</properties>
```

### 2.1 详细说明

| 属性名 | 值 | 说明 | 使用位置 |
|-------|---|------|---------|
| `java.version` | `1.8` | Java 编译版本 | `maven-compiler-plugin`、Spring Boot 配置 |
| `project.build.sourceEncoding` | `UTF-8` | 源码文件编码 | 编译器读取源文件 |
| `project.reporting.outputEncoding` | `UTF-8` | 报告输出编码 | Maven 站点生成 |
| `spring-boot.version` | `2.6.13` | Spring Boot 版本号 | `dependencyManagement` 导入 BOM |

### 2.2 为什么使用属性？

**好处：**
1. **版本集中管理** - 一处修改，全局生效
2. **避免重复** - 相同版本号只写一次
3. **可读性好** - 清晰知道使用的版本
4. **便于升级** - 统一升级框架版本

**示例对比：**

```xml
<!-- 不使用属性 - 升级时需要修改多处 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.6.13</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>2.6.13</version>
</dependency>

<!-- 使用属性 - 升级只需改一处 -->
<properties>
    <spring-boot.version>2.6.13</spring-boot.version>
</properties>
<!-- 后面会用 dependencyManagement 管理 -->
```

---

## 📦 3. Dependencies 依赖管理

### 3.1 依赖的基本结构

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>...</version>
    <scope>...</scope>
    <optional>...</optional>
</dependency>
```

### 3.2 项目依赖详解

#### A. Web 核心框架

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

| 项目 | 说明 |
|------|------|
| **groupId** | `org.springframework.boot` |
| **artifactId** | `spring-boot-starter-web` |
| **包含内容** | Spring MVC + Tomcat + Jackson JSON |
| **作用** | 提供 Web 开发所需的一切 |
| **版本来源** | `dependencyManagement` 中的 BOM |

**传递依赖示例：**
- `spring-boot-starter` (核心启动器)
- `spring-web` (Web 支持)
- `spring-webmvc` (MVC 框架)
- `spring-boot-starter-tomcat` (Tomcat 容器)
- `jackson-databind` (JSON 处理)

---

#### B. 数据库访问 - MyBatis

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **作用** | ORM 框架，简化数据库操作 |
| **显式版本** | `2.2.2` (不在 Spring Boot BOM 中) |
| **功能** | SQL 映射、结果映射、动态 SQL |

---

#### C. MySQL 数据库驱动

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

| 配置 | 说明 |
|------|------|
| **scope** | `runtime` - 只在运行时需要，编译时不需要 |
| **作用** | JDBC 驱动，连接 MySQL 数据库 |
| **版本来源** | Spring Boot BOM 管理 |

**Scope 说明：**

| scope 值 | 何时有效 | 示例 |
|---------|---------|------|
| `compile` | 编译、测试、运行、打包 | 大多数依赖 |
| `provided` | 编译、测试，不打包 | Servlet API |
| `runtime` | 测试、运行，打包，不参与编译 | JDBC 驱动 |
| `test` | 仅测试 | JUnit |
| `system` | 类似 provided，但指定本地路径 | 外部 JAR |

---

#### D. Lombok 代码简化工具

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

| 配置 | 说明 |
|------|------|
| **optional** | `true` - 可选依赖，不传递给依赖此项目的其他模块 |
| **作用** | 通过注解自动生成 getter/setter/toString 等 |

**使用示例：**
```java
import lombok.Data;

@Data  // 自动生成 getter, setter, toString, hashCode, equals
public class User {
    private Long id;
    private String name;
}
```

---

#### E. 测试依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **scope** | `test` - 仅测试阶段使用，不会打包进最终 JAR |
| **包含内容** | JUnit 5 + Spring Test + Mockito + AssertJ |

---

#### F. 分页插件 PageHelper

```xml
<!--分页插件PageHelper-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.7</version>
</dependency>
```

| 项目 | 说明 |
|------|------|
| **作用** | MyBatis 物理分页插件，简化分页开发 |
| **使用方式** | `PageHelper.startPage(pageNum, pageSize);` |
| **显式版本** | `1.4.7` - 版本单独指定 |

---

#### G. 阿里云 OSS 对象存储

```xml
<!--阿里云OSS依赖-->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.17.4</version>
</dependency>
```

| 项目 | 说明 |
|------|------|
| **作用** | 阿里云对象存储服务 SDK |
| **用途** | 文件上传、下载、删除等 |

---

#### H. JAXB (Java Architecture for XML Binding)

```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.3</version>
</dependency>
```

**背景说明：**
- Java 9+ 中 JAXB 从 JDK 中移除
- 必须显式引入这些依赖
- 用于 XML 绑定

---

#### I. JWT (JSON Web Token) 认证

```xml
<!-- JWT依赖-->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

**依赖划分：**

| 依赖 | 作用 | Scope |
|------|------|-------|
| `jjwt-api` | API 接口定义 | compile |
| `jjwt-impl` | 实现类 | runtime |
| `jjwt-jackson` | Jackson JSON 序列化 | runtime |

**使用示例：**
```java
// 生成 Token
String jwt = Jwts.builder()
    .setSubject("userId")
    .signWith(key)
    .compact();
```

---

#### J. AOP 面向切面编程

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

| 作用 | 包含内容 |
|------|---------|
| 切面编程 | Spring AOP + AspectJ |
| **用途** | 日志记录、事务管理、权限控制、性能监控 |

**示例：**
```java
@Aspect
@Component
public class LogAspect {
    @Around("@annotation(Log)")
    public Object log(ProceedingJoinPoint point) {
        // 记录操作日志
    }
}
```

---

#### K. Spring Security 密码加密

```xml
<!-- BCrypt密码加密 -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
    <version>5.6.3</version>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **用途** | 提供 BCrypt 等密码加密算法 |
| **注意** | 只引入 crypto 模块，不引入完整 Security |

**使用示例：**
```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashed = encoder.encode("userPassword");
```

---

#### L. 参数校验 Validation

```xml
<!-- 参数校验 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

| 作用 | 包含内容 |
|------|---------|
| 参数验证 | Hibernate Validator (JSR-380 实现) |

**使用示例：**
```java
public class User {
    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 10)
    private String name;
    
    @Email
    private String email;
}
```

---

#### M. Redis 缓存

```xml
<!-- Redis缓存 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

| 内容 | 说明 |
|------|------|
| Lettuce | Redis 客户端驱动 |
| Spring Data Redis | Redis 操作模板 |

---

#### N. Spring Cache 缓存抽象

```xml
<!-- Spring Cache -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

| 作用 | 注解 |
|------|------|
| 声明式缓存 | `@Cacheable`, `@CacheEvict`, `@CachePut` |

---

#### O. RabbitMQ 消息队列

```xml
<!-- RabbitMQ消息队列 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

| 作用 | 包含内容 |
|------|---------|
| AMQP 消息 | Spring AMQP + RabbitMQ 客户端 |

---

#### P. Redisson 分布式锁

```xml
<!-- Redisson分布式锁 -->
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.23.5</version>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **作用** | 高级 Redis 客户端，提供分布式锁等功能 |
| **显式版本** | `3.23.5` |

---

#### Q. Google Guava (布隆过滤器)

```xml
<!-- 布隆过滤器 -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>32.1.3-jre</version>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **作用** | 提供布隆过滤器、集合工具类等 |
| **版本后缀** | `-jre` 表示适合 Java 8+ |

---

#### R. Knife4j 接口文档

```xml
<!-- Swagger/Knife4j 接口文档 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

| 特点 | 说明 |
|------|------|
| **作用** | Swagger 的增强版本，更好的中文界面 |
| **访问地址** | `http://host:port/doc.html` |

---

## 📊 4. DependencyManagement 依赖版本管理

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 4.1 工作原理

**dependencyManagement 的作用：**
1. **版本集中管理** - 定义依赖的默认版本
2. **不实际引入依赖** - 只是声明版本，不会下载 JAR
3. **子模块继承** - 子项目无需再写版本号

**import scope 说明：**

```xml
<type>pom</type>
<scope>import</scope>
```

这种方式用于导入另一个 POM 的 dependencyManagement。

**Spring Boot BOM (Bill of Materials)：**
- `spring-boot-dependencies` 是一个 POM 类型依赖
- 包含所有 Spring Boot 官方依赖的版本
- 导入后，使用时就不需要写版本号了

### 4.2 完整对比示例

**不使用 dependencyManagement：**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.6.13</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
        <version>2.6.13</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
        <version>2.6.13</version>
    </dependency>
    <!-- 几十个依赖，每个都要写版本号，升级时要改几十处 -->
</dependencies>
```

**使用 dependencyManagement：**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.6.13</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <!-- 版本统一由 BOM 管理，升级只需改版本号一处 -->
</dependencies>
```

---

## 🔨 5. Build 构建配置

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${spring-boot.version}</version>
            <configuration>
                <mainClass>com.lcy.TliasWebManagementApplication</mainClass>
                <skip>true</skip>
            </configuration>
            <executions>
                <execution>
                    <id>repackage</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 5.1 maven-compiler-plugin 编译插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <encoding>UTF-8</encoding>
    </configuration>
</plugin>
```

| 配置 | 说明 |
|------|------|
| `source` | 源代码 Java 版本 |
| `target` | 生成字节码的 Java 版本 |
| `encoding` | 源文件编码 |

**执行命令：**
```bash
mvn compile          # 编译
mvn clean compile    # 清理后编译
```

### 5.2 spring-boot-maven-plugin Spring Boot 插件

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>${spring-boot.version}</version>
    <configuration>
        <mainClass>com.lcy.TliasWebManagementApplication</mainClass>
        <skip>true</skip>
    </configuration>
    <executions>
        <execution>
            <id>repackage</id>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

#### 配置详解

| 配置项 | 值 | 说明 |
|-------|---|------|
| `mainClass` | `com.lcy.TliasWebManagementApplication` | 启动类，执行 `java -jar` 时的入口 |
| `skip` | `true` | 跳过此插件的默认执行 |
| `goal` | `repackage` | 创建可执行 JAR |

#### repackage 目标的作用

**不使用 repackage：**
```bash
mvn package
# 生成的 JAR 很小，不能直接运行
# 包含：仅项目的 class
```

**使用 repackage：**
```bash
mvn package
# 生成可执行的 FAT JAR
# 包含：项目 class + 所有依赖 JAR
# 可直接运行：java -jar app.jar
```

#### 生成的文件

执行 `mvn package` 后：
```
target/
├── tlias-web-management-0.0.1-SNAPSHOT.jar          # 原始 JAR (小)
├── tlias-web-management-0.0.1-SNAPSHOT.jar.original # 备份的原始 JAR
└── tlias-web-management-0.0.1-SNAPSHOT.jar          # 可执行 JAR (大)
```

#### 常用命令

```bash
# 编译
mvn compile

# 打包
mvn package

# 跳过测试打包
mvn package -DskipTests

# 运行项目
mvn spring-boot:run

# 清理构建
mvn clean
```

---

## ✅ 6. 最佳实践建议

### 6.1 依赖管理最佳实践

1. **使用 properties 管理版本**
   ```xml
   <properties>
       <mybatis.version>2.2.2</mybatis.version>
   </properties>
   
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>${mybatis.version}</version>
   </dependency>
   ```

2. **使用 dependencyManagement 管理版本**
   - 使用 BOM 导入
   - 避免重复版本号

3. **范围(scope)使用恰当**
   - JDBC 驱动用 `runtime`
   - 测试依赖用 `test`
   - 可选依赖用 `optional`

4. **排除不需要的依赖**
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
       <exclusions>
           <exclusion>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-logging</artifactId>
           </exclusion>
       </exclusions>
   </dependency>
   ```

### 6.2 构建最佳实践

1. **使用 profile 管理环境配置**
   ```xml
   <profiles>
       <profile>
           <id>dev</id>
           <properties>
               <env>dev</env>
           </properties>
       </profile>
       <profile>
           <id>prod</id>
           <properties>
               <env>prod</env>
           </properties>
       </profile>
   </profiles>
   ```

   使用：`mvn package -Pprod`

2. **资源过滤**
   ```xml
   <build>
       <resources>
           <resource>
               <directory>src/main/resources</directory>
               <filtering>true</filtering>
           </resource>
       </resources>
   </build>
   ```

3. **指定最终名称**
   ```xml
   <build>
       <finalName>tlias-app</finalName>
   </build>
   ```

### 6.3 版本号规范

```
  主版本.次版本.修订版-标记
  │     │      │      │
  │     │      │      └─ SNAPSHOT / RC1 / RELEASE
  │     │      └──────── 问题修复
  │     └─────────────── 新功能
  └───────────────────── 大版本变更
```

**示例：**
- `0.0.1-SNAPSHOT` - 开发中
- `1.0.0-RC1` - 发布候选 1
- `1.0.0-RELEASE` - 正式发布
- `1.1.0-RELEASE` - 新增功能
- `1.1.1-RELEASE` - 问题修复

---

## 📝 总结

### 完整 Maven 构建流程

```bash
mvn clean          # 清理 target 目录
    ↓
mvn compile        # 编译源码
    ↓
mvn test           # 运行测试
    ↓
mvn package        # 打包
    ↓
mvn install        # 安装到本地仓库
    ↓
mvn deploy         # 发布到远程仓库
```

### 项目依赖关系图

```
tlias-web-management
├── spring-boot-starter-web (Web MVC)
├── mybatis-spring-boot-starter (数据库 ORM)
├── spring-boot-starter-data-redis (缓存)
├── spring-boot-starter-amqp (消息队列)
├── redisson-spring-boot-starter (分布式锁)
├── knife4j-spring-boot-starter (API 文档)
├── jjwt (身份认证)
└── 其他工具类依赖
```

### 关键文件位置

```
tlias-web-management/
├── pom.xml                                    # Maven 配置文件
├── src/
│   ├── main/
│   │   ├── java/com/lcy/
│   │   │   └── TliasWebManagementApplication.java
│   │   └── resources/
│   │       ├── application.yml               # 主配置
│   │       └── application-vm.yml            # VM 环境配置
│   └── test/
└── target/                                    # 构建输出
    └── tlias-web-management-0.0.1-SNAPSHOT.jar
```

---

## 🔗 参考资料

| 资源 | 链接 |
|------|------|
| Maven 官方文档 | https://maven.apache.org/guides/ |
| Spring Boot POM 介绍 | https://spring.io/projects/spring-boot |
| Maven 依赖机制 | https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html |
