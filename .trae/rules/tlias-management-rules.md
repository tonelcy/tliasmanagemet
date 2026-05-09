---
alwaysApply: true
---
# tlias-web-management 开发规范

---

## 0. 项目技术栈概述

### 后端技术栈
- **框架**：Spring Boot 2.6.13
- **持久层**：MyBatis + PageHelper
- **数据库**：MySQL
- **工具库**：Lombok
- **认证**：JWT (jjwt 0.11.5)
- **文件存储**：阿里云 OSS
- **日志**：SLF4J + Logback
- **Java版本**：Java 1.8

### 前端技术栈
- **框架**：Vue 3
- **UI组件库**：Element Plus
- **路由**：Vue Router
- **HTTP客户端**：Axios
- **构建工具**：Vite

---

## 1. 项目结构规范

### 后端结构 (Spring Boot)
```
src/main/java/com/lcy/
├── controller/        # 控制器层：处理HTTP请求
├── service/           # 服务接口层
│   └── impl/          # 服务实现层
├── mapper/            # 数据访问层（MyBatis）
├── pojo/              # 实体类、VO、DTO
├── utils/             # 工具类
├── exception/         # 异常处理
├── filter/            # 过滤器
├── interceptor/       # 拦截器
├── aop/               # 切面（AOP）
├── anno/              # 自定义注解
└── WebConfig/         # 配置类

src/main/resources/
├── com/lcy/mapper/    # MyBatis XML映射文件
├── static/            # 静态资源
├── application.yml    # 配置文件
└── logback.xml        # 日志配置
```

### 前端结构 (Vue 3)
```
front/tilas-management/src/
├── api/               # API接口封装
│   ├── emp.js
│   ├── dept.js
│   ├── login.js
│   └── register.js
├── views/             # 页面组件
│   ├── emp/
│   │   └── index.vue
│   ├── dept/
│   │   └── index.vue
│   ├── login/
│   └── ...
├── components/        # 公共组件
├── router/            # 路由配置
│   └── index.js
├── utils/             # 工具函数
│   └── request.js
├── assets/            # 静态资源
├── App.vue
└── main.js
```

---

## 2. 代码命名规范

### 后端 (Java)
- **类名**：大驼峰（PascalCase），如 `EmpController`、`EmpService`、`GlobalExceptionHandler`
- **方法名**：小驼峰（camelCase），如 `page()`、`save()`、`getInfo()`、`deleteByIds()`
- **变量名**：小驼峰（camelCase），如 `empList`、`pageResult`、`loginInfo`
- **常量**：全大写下划线分隔，如 `SECRET_KEY`、`EXPIRE_TIME`
- **包名**：全小写，如 `com.lcy.controller`
- **POJO类**：业务实体名，如 `Emp`、`Dept`、`OperateLog`

### 前端 (JavaScript/Vue)
- **组件名**：大驼峰，如 `EmpList.vue`、`UserList.vue`、`index.vue`（页面级组件可用小写）
- **函数名**：小驼峰，如 `queryPageApi()`、`search()`、`handleSizeChange()`
- **变量名**：小驼峰，如 `empList`、`searchEmp`、`dialogVisible`
- **常量**：全大写下划线分隔（可选），如 `JOBS`、`GENDERS`
- **API函数**：动词+名词+Api，如 `queryPageApi`、`addApi`、`deleteApi`

---

## 3. 代码注释标准

### Java注释规范
- **类级别**：简要说明类的功能
  ```java
  /**
   * 员工控制器
   */
  @RestController
  @RequestMapping("/emps")
  public class EmpController {
  }
  ```
- **方法级别**：说明方法功能、参数、返回值
  ```java
  /**
   * 分页查询员工数据
   * @param empQueryParam 查询参数
   * @return 分页结果
   */
  @GetMapping
  public Result page(EmpQueryParam empQueryParam) {
  }
  ```
- **字段级别**：简要说明字段含义（使用行内注释）
  ```java
  @Data
  public class Result {
      private Integer code; //编码：1成功，0为失败
      private String msg; //错误信息
      private Object data; //数据
  }
  ```
- **保留代码**：重要历史代码可注释保留，标注清楚用途
  ```java
  //原始分页查询
  //@GetMapping
  //public Result list() {
  //    List<Emp> empList = empService.list();
  //    log.info("查询员工列表" + empList);
  //    return Result.success();
  //}
  ```

### 前端注释规范
- **API函数**：简洁说明功能
  ```javascript
  //查询员工列表数据
  export const queryPageApi = (name, gender, begin, end, page, pageSize) => 
    request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
  ```
- **复杂逻辑**：使用单行或多行注释说明
  ```javascript
  //侦听searchEmp的date属性
  watch(() => searchEmp.value.date, (newVal, oldVal) => {
    if (newVal.length == 2) {
      searchEmp.value.begin = newVal[0];
      searchEmp.value.end = newVal[1];
    } else {
      searchEmp.value.begin = '';
      searchEmp.value.end = '';
    }
  })
  ```

---

## 4. 分层开发规范

### Controller 层
- 使用 `@RestController` + `@RequestMapping` 定义路由
- 统一使用 `Result` 封装返回结果
- 使用 `@Slf4j` 记录日志
- 请求参数接收：
  - JSON：`@RequestBody`
  - 查询参数：`@RequestParam`
  - 路径变量：`@PathVariable`
- 不写业务逻辑，只做参数接收、调用Service、返回结果
- RESTful风格：
  - GET：查询
  - POST：新增
  - PUT：修改
  - DELETE：删除

**示例**：
```java
@RestController
@Slf4j
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("查询员工信息: {}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("请求参数emp: {}", emp);
        empService.save(emp);
        return Result.success();
    }
}
```

### Service 层
- 接口定义业务方法
- 实现类使用 `@Service` 注解
- 使用 `@Transactional(rollbackFor = Exception.class)` 保证事务一致性
- 使用 `@Autowired` 注入依赖
- 业务逻辑写在Service层
- 涉及多个Mapper操作时必须加事务

**示例**：
```java
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        //补全基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
        //保存员工工作经历信息
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }
}
```

### Mapper 层
- 使用 `@Mapper` 注解
- 简单SQL用注解：`@Select`、`@Insert`、`@Update`、`@Delete`
- 复杂SQL用XML文件（放 `src/main/resources/com/lcy/mapper/`）
- 使用 `@Param` 标记参数
- 主键返回使用 `@Options(useGeneratedKeys = true, keyProperty = "id")`
- 统计数据使用 `@MapKey` 指定key

**示例**：
```java
@Mapper
public interface EmpMapper {
    //复杂查询使用XML
    List<Emp> list(EmpQueryParam empQueryParam);

    //简单查询使用注解
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    //批量操作
    void deleteByIds(@Param("ids") List<Integer> ids);

    //统计数据
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();
}
```

---

## 5. POJO/DTO/VO 规范

### 分类说明
- **POJO/Entity**：数据库表对应实体，使用 `@Data` 注解
- **DTO**：数据传输对象，用于接收前端参数
- **VO**：视图对象，用于返回前端展示
- **PageResult**：分页结果封装
- **Result**：统一返回结果

### 示例
```java
@Data
public class Emp {
    private Integer id;
    private String username;
    private String name;
    private Integer gender;
    private String phone;
    private Integer job;
    private Integer salary;
    private String image;
    private LocalDate entryDate;
    private Integer deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    //扩展字段
    private String deptName;
    private List<EmpExpr> exprList;
}

@Data
public class EmpQueryParam {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer gender;
    private LocalDate begin;
    private LocalDate end;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private Long total;
    private List<T> rows;
}
```

---

## 6. 错误处理规范

### 全局异常处理
- 使用 `@RestControllerAdvice` + `@ExceptionHandler`
- 通用异常统一处理，返回友好提示
- 特定异常单独处理，如 `DuplicateKeyException`
- 所有异常必须记录日志

**示例**：
```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("服务器异常: {}", e.getMessage(), e);
        return Result.error("服务器异常,请联系管理员");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据库异常: {}", e.getMessage(), e);
        return Result.error("数据已存在");
    }
}
```

### 返回结果规范
- 统一使用 `Result` 封装
  - `code`：1成功，0失败
  - `msg`：提示信息
  - `data`：返回数据
- 成功调用 `Result.success()` 或 `Result.success(data)`
- 失败调用 `Result.error(msg)`

```java
@Data
public class Result {
    private Integer code; //编码：1成功，0为失败
    private String msg; //错误信息
    private Object data; //数据

    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }
}
```

---

## 7. 日志规范

### 日志使用
- 使用 `@Slf4j` 注解（Lombok提供）
- 日志级别：
  - `log.info()`：普通业务日志
  - `log.error()`：异常错误日志
  - `log.debug()`：调试日志
- 日志内容要包含关键参数信息

**示例**：
```java
@Slf4j
@RestController
public class EmpController {
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("查询员工信息: {}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }
}
```

---

## 8. 测试规范
- 测试类放在 `src/test/java/`
- 测试类命名：`XxxTest`
- 测试方法命名：`testXxx`
- 使用 `@SpringBootTest` 注解
- 测试前后保持数据一致性
- 测试代码独立，不依赖其他测试

**示例**：
```java
@SpringBootTest
public class JwtTest {
    @Test
    public void testGenerateJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "admin");
        String jwt = JwtUtils.generateJwt(claims);
        System.out.println(jwt);
    }
}
```

---

## 9. 版本控制与Git规范

### 提交信息规范
```
feat: 新增功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构代码
test: 测试相关
chore: 构建/工具相关
```

**示例**：
```
feat: 新增员工管理模块
fix: 修复分页查询bug
docs: 更新开发规范文档
```

### 忽略文件
- 使用现有 `.gitignore`
- 新增忽略项及时更新
- 不要提交配置文件中的密码、密钥等敏感信息

---

## 10. 配置文件规范

### application.yml
- 敏感信息（密码、密钥）配置化，不硬编码
- 使用分层缩进
- 注释清晰说明配置项用途
- 使用配置类读取自定义配置

**示例**：
```yaml
spring:
  application:
    name: tlias-web-management
  datasource:
    url: jdbc:mysql://localhost:3306/tlias
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 1234
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
pagehelper:
  reasonable: true
  helper-dialect: mysql
aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com
    bucketName: javaweb1-ai-01
    region: cn-beijing
```

### MyBatis配置
- 开启驼峰命名转换：`map-underscore-to-camel-case: true`
- 开发环境开启SQL日志：`log-impl`
- 数据库字段名：下划线命名（如 `dept_id`）
- Java属性名：驼峰命名（如 `deptId`）

### 配置属性类
```java
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {
    private String endpoint;
    private String bucketName;
    private String region;
}
```

---

## 11. 数据库操作规范

### 分页查询
- 使用 PageHelper 分页插件
- 在查询前调用 `PageHelper.startPage(page, pageSize)`
- 返回结果封装为 `PageResult`

**示例**：
```java
public PageResult<Emp> page(EmpQueryParam empQueryParam) {
    PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
    List<Emp> empList = empMapper.list(empQueryParam);
    Page<Emp> p = (Page<Emp>) empList;
    return new PageResult<>(p.getTotal(), p.getResult());
}
```

### 批量操作
- 批量插入使用 `insertBatch`
- 批量删除使用 `deleteByIds`
- 批量操作要考虑性能和事务

### 事务管理
- 使用 `@Transactional(rollbackFor = Exception.class)`
- 涉及多个表操作时必须加事务
- Service层方法加事务注解

---

## 12. 性能优化指南

### 分页查询
- 使用 PageHelper 分页插件
- 避免一次查询大量数据
- 合理设置每页条数

### 数据库操作
- 批量操作使用 `insertBatch`、`deleteByIds`
- 合理使用事务 `@Transactional`
- 优化SQL，避免全表扫描
- 合理使用索引
- 避免N+1查询问题

### 前端优化
- 按需加载组件
- 合理使用 `watch` 和 `computed`
- 图片大小限制（本项目10M以内）
- 列表数据分页展示

---

## 13. 安全规范

### JWT认证
- 使用强密钥生成JWT
- 设置合理的过期时间（本项目12小时）
- 通过拦截器/过滤器统一验证Token
- Token放在请求头中

**JWT工具类**：
```java
public class JwtUtils {
    private static final String SECRET_STRING = "LCYDYWLCYDYWLCYDYWLCYDYWLCYDYWLCYDYW";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    private static Long expire = 43200000L; //12小时

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }
}
```

### 其他安全要点
- **密码**：不在代码中硬编码密码
- **接口鉴权**：通过拦截器/过滤器统一处理
- **输入校验**：前端表单校验 + 后端参数校验
- **文件上传**：限制文件类型（jpg/png）和大小（10M以内）
- **SQL注入**：使用MyBatis参数化查询，避免字符串拼接

---

## 14. 代码复用原则

- **公共方法**：抽取到 `utils` 包
- **公共组件**：前端抽取到 `components` 目录
- **公共API**：统一封装在 `api` 目录
- **业务逻辑复用**：Service层抽取公共方法
- **工具类**：提供静态方法，无状态

---

## 15. 前端开发规范

### Vue 3 Composition API
- 使用 `<script setup>` 语法
- 响应式数据使用 `ref()` 或 `reactive()`
- 生命周期使用 `onMounted()`、`onUnmounted()` 等
- 使用 `watch` 监听数据变化（可设置 `deep: true` 深度监听）

**示例**：
```vue
<script setup>
import { ref, watch, onMounted } from 'vue'

const empList = ref([])
const searchEmp = ref({ name: '', gender: '', date: [] })

watch(() => searchEmp.value.date, (newVal, oldVal) => {
  if (newVal.length == 2) {
    searchEmp.value.begin = newVal[0]
    searchEmp.value.end = newVal[1]
  }
})

onMounted(() => {
  search()
})
</script>
```

### Element Plus 使用
- 统一使用 Element Plus 组件库
- 表单校验使用 rules 配置
- 消息提示使用 ElMessage
- 确认对话框使用 ElMessageBox

**示例**：
```javascript
import { ElMessage, ElMessageBox } from 'element-plus'

//消息提示
ElMessage.success('操作成功')
ElMessage.error('操作失败')

//确认对话框
ElMessageBox.confirm('确认删除吗?', '提示', {
  confirmButtonText: '确认',
  cancelButtonText: '取消',
  type: 'warning'
}).then(async () => {
  await deleteApi(id)
  ElMessage.success('删除成功')
})
```

### Axios 封装
- 使用 `request.js` 封装的 axios 实例
- 统一处理请求/响应拦截
- Token 自动在请求头携带
- 基础URL设置为 `/api`
- 超时时间设置为600000ms

**request.js**：
```javascript
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

request.interceptors.request.use((config) => {
  let loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser) {
    config.headers.token = loginUser.token
  }
  return config
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response.status === 401) {
      ElMessage.error('登录失效, 请重新登录')
      router.push('/login')
    } else {
      ElMessage.error('接口访问异常')
    }
    return Promise.reject(error)
  }
)

export default request
```

### API 封装
- API函数按业务模块分类
- 每个模块一个js文件
- 函数命名：动词+名词+Api

**emp.js 示例**：
```javascript
import request from "@/utils/request"

export const queryPageApi = (name, gender, begin, end, page, pageSize) => 
  request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)

export const addApi = (emp) => request.post('/emps', emp)

export const queryInfoApi = (id) => request.get(`/emps/${id}`)

export const updateApi = (emp) => request.put('/emps', emp)

export const deleteApi = (ids) => request.delete(`/emps?ids=${ids}`)
```

### Vue 文件结构
```vue
<script setup>
// 1. 导入
// 2. 响应式数据定义
// 3. watch 监听
// 4. 生命周期钩子
// 5. 方法定义
</script>

<template>
<!-- 模板内容 -->
</template>

<style scoped>
/* 样式 */
</style>
```

---

## 16. AOP 切面规范

### 使用场景
- 操作日志记录
- 性能监控
- 事务管理（已通过 `@Transactional` 实现）

### 示例（操作日志）
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";
}

@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    @Around("@annotation(log)")
    public Object recordLog(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("操作: {}, 耗时: {}ms", log.value(), (end - start));
        return result;
    }
}
```

---

## 17. 文件上传规范

### 后端
- 使用阿里云 OSS 存储文件
- 文件上传接口返回文件访问URL
- 限制文件类型和大小

### 前端
- 文件大小限制：10M以内
- 文件类型限制：jpg、png
- 使用 Element Plus Upload 组件
- 上传成功后将URL赋值给表单字段

**示例**：
```vue
<el-upload 
  class="avatar-uploader" 
  action="/api/upload" 
  :headers="{ 'token': token }"
  :show-file-list="false" 
  :on-success="handleAvatarSuccess"
  :before-upload="beforeAvatarUpload">
  <img v-if="employee.image" :src="employee.image" class="avatar" />
  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
</el-upload>

<script setup>
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}
</script>
```

---

## 18. 快速开发检查清单

### 新增功能检查
- [ ] Controller层：路由、参数接收、日志
- [ ] Service层：业务逻辑、事务
- [ ] Mapper层：SQL操作
- [ ] POJO类：实体定义
- [ ] 前端API：接口封装
- [ ] 前端页面：组件实现
- [ ] 异常处理：全局异常
- [ ] 测试：功能验证

---

**文档版本**：v1.0  
**最后更新**：2026-05-05
