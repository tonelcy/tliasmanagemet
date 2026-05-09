# GitHub仓库管理指南

## 项目概述
tlias-web-management - 智能学习辅助系统

---

## 一、Git仓库初始化

### 1.1 检查Git状态
```bash
git status
```

### 1.2 初始化仓库（如果还没有）
```bash
git init
```

### 1.3 创建.gitignore文件
在项目根目录创建`.gitignore`：
```
# 编译文件
target/
*.class
*.jar
*.war

# IDE配置
.idea/
*.iml
.vscode/

# 日志
logs/
*.log

# 系统文件
.DS_Store
Thumbs.db

# 临时文件
*.tmp
*.bak
*.swp
```

---

## 二、分支结构规划

### 2.1 标准分支
- **main/master** - 生产环境分支，受保护，只接受PR合并
- **develop** - 开发分支，集成稳定的开发功能
- **feature/*** - 功能分支，从develop创建，功能完成后合并回develop
- **bugfix/*** - 修复分支，用于修复生产环境或开发环境的问题
- **hotfix/*** - 紧急修复分支，从main创建，修复后合并到main和develop

### 2.2 分支创建流程

```bash
# 创建main分支
git checkout -b main
git commit -m "Initial commit"

# 创建develop分支
git checkout -b develop
git merge main --no-edit

# 创建功能分支
git checkout -b feature/login-functionality
```

---

## 三、首次提交与远程推送

### 3.1 首次提交
```bash
# 添加所有文件
git add .

# 创建首次提交
git commit -m "Initial commit: tlias-web-management project"
```

### 3.2 在GitHub创建仓库

**方式1：通过GitHub网页创建**
1. 访问 https://github.com/new
2. 仓库名称：`tlias-web-management`
3. 选择公开或私有
4. 点击"Create repository"

**方式2：使用GitHub CLI（如果已安装）**
```bash
gh repo create tlias-web-management --private --source=. --remote=origin
```

### 3.3 添加远程仓库
```bash
# 添加远程仓库（替换USERNAME为您的GitHub用户名）
git remote add origin https://github.com/USERNAME/tlias-web-management.git
```

### 3.4 推送所有分支
```bash
# 推送main分支
git push -u origin main

# 推送develop分支
git checkout develop
git push -u origin develop
```

---

## 四、分支保护规则（GitHub设置）

进入仓库设置 → Branches → Add a branch protection rule：

### 保护main分支规则
- [x] Require pull request reviews before merging
- [x] Require status checks to pass before merging
- [x] Require signed commits（可选）
- [x] Include administrators（可选）

### 保护develop分支规则
- [x] Require pull request reviews before merging（可选）
- [x] Require status checks to pass before merging

---

## 五、Issues模板创建

在项目根目录创建 `.github/ISSUE_TEMPLATE/` 文件夹

### bug_report.md - Bug报告模板
```markdown
---
name: Bug报告
about: 报告一个问题，帮助我们改进
title: "[BUG] "
labels: bug
assignees: ''

---

## 描述问题
简单清晰地描述问题是什么。

## 复现步骤
1. 前往 ...
2. 点击 ...
3. 看到错误

## 预期行为
清晰描述你期望发生什么。

## 实际行为
实际发生了什么。

## 环境信息
- 系统版本: [例如 Windows 11]
- JDK版本: [例如 1.8]
- 浏览器版本: [如果适用]

## 截图或日志
如果有，可以添加截图或日志片段。
```

### feature_request.md - 功能请求模板
```markdown
---
name: 功能请求
about: 提出一个新功能建议
title: "[FEATURE] "
labels: enhancement
assignees: ''

---

## 功能描述
请详细描述您想要的功能。

## 使用场景
请说明这个功能在什么情况下会用到。

## 预期效果
说明您期望这个功能实现后的效果。

## 补充说明
如果有其他信息，可以在此说明。
```

### task_template.md - 任务模板
```markdown
---
name: 任务分配
about: 分配开发或运维任务
title: "[TASK] "
labels: task
assignees: ''

---

## 任务描述
请描述任务内容。

## 交付标准
任务完成的标准。

## 时间安排
- 预期完成日期：
- 优先级：高/中/低

## 相关资源
如果有相关文档或资源，请在此列出。
```

---

## 六、开发工作流

### 6.1 日常开发流程
```bash
# 1. 切换到develop分支
git checkout develop
git pull

# 2. 创建功能分支
git checkout -b feature/your-feature-name

# 3. 进行开发
# ... 修改代码 ...

# 4. 提交代码
git add .
git commit -m "feature: 简要描述功能"

# 5. 推送到远程
git push origin feature/your-feature-name
```

### 6.2 PR（Pull Request）流程
1. 在GitHub上创建PR，指向develop分支
2. 等待代码审查
3. 根据反馈进行修改
4. 合并到develop

### 6.3 发布流程
```bash
# 1. 切换到main
git checkout main
git pull

# 2. 合并develop到main
git merge develop

# 3. 创建版本标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 4. 推送
git push origin main --tags
```

---

## 七、常用Git命令速查

### 基础操作
```bash
# 查看状态
git status

# 查看历史
git log --oneline

# 创建分支
git checkout -b branch-name

# 切换分支
git checkout branch-name

# 删除分支
git branch -d branch-name
```

### 远程操作
```bash
# 查看远程仓库
git remote -v

# 拉取最新
git pull origin branch-name

# 推送
git push origin branch-name
```

---

## 八、GitHub协作最佳实践

1. **每次提交要小而聚焦** - 一个提交只包含一个功能或修复
2. **提交信息要清晰** - 使用动词开头，如 "feature:" "fix:" "docs:"
3. **及时Push分支** - 避免代码只存在本地
4. **定期同步develop** - 保持代码最新，减少冲突
5. **使用Issues跟踪** - 用Issues记录需求和问题
6. **代码审查** - 通过PR合并代码，确保代码质量

---

## 九、项目文件说明

- `.trae/documents/` - 项目相关文档
- `src/main/` - 源代码目录
- `pom.xml` - Maven配置文件
- `README.md` - 项目说明
