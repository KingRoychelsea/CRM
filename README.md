# CRM系统运行说明文档

## 一、项目简介

本CRM系统是一套通用、可落地、易扩展的全栈解决方案，基于SpringBoot 2.7.x + Vue3 + MyBatis-Plus 3.5.x + SpringSecurity 5.7.x技术栈开发，包含完整的系统管理、客户管理、销售管理和数据统计分析功能模块。

## 二、环境要求

### 1. 后端环境
- JDK 1.8+
- Maven 3.8.x
- MySQL 8.0+
- Redis 6.0+（可选，用于缓存）

### 2. 前端环境
- Node.js 14.x+
- npm 6.x+ 或 yarn 1.22.x+

## 三、部署步骤

### 1. 数据库部署
1. 创建数据库：`CREATE DATABASE crm DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. 执行数据库脚本：`database-init.sql`，创建所有表结构和基础数据
3. 数据库连接配置：
   - 连接地址：127.0.0.1:3306
   - 用户名：root
   - 密码：******
   - 数据库名称：crm

   > 注意：请根据实际环境修改数据库连接配置，在 `backend/src/main/resources/application.yml` 文件中更新数据库连接信息。

### 2. 后端项目部署
1. 进入后端目录：`cd backend`
2. 安装依赖：`D:\maven\apache-maven-3.8.8\bin\mvn clean install`
3. 启动项目：`D:\maven\apache-maven-3.8.8\bin\mvn spring-boot:run` 或使用IDE运行 `CrmApplication.java`
4. 后端服务默认运行在 `http://localhost:8080`

### 3. 前端项目部署
1. 进入前端目录：`cd frontend`
2. 安装依赖：`npm install` 或 `yarn install`
3. 开发环境启动：`npm run dev` 或 `yarn dev`
4. 生产环境构建：`npm run build` 或 `yarn build`，构建产物在 `dist` 目录
5. 前端服务默认运行在 `http://localhost:5173`

## 四、测试账号

### 1. 管理员账号
- 用户名：admin
- 密码：123456
- 权限：拥有所有系统功能权限

### 2. 普通用户账号
- 用户名：user
- 密码：123456
- 权限：拥有基础的客户管理和销售管理权限

## 五、核心接口说明

### 1. 认证接口
- 登录：`POST /api/auth/login`，参数：username, password
- 登出：`POST /api/auth/logout`
- 刷新token：`POST /api/auth/refresh`

### 2. 系统管理接口
- 用户管理：`/api/sys/user`，支持增删改查、分页查询
- 角色管理：`/api/sys/role`，支持角色增删改查、权限分配
- 权限管理：`/api/sys/menu`，支持菜单权限管理
- 部门管理：`/api/sys/dept`，支持部门树形结构管理
- 系统日志：`/api/sys/log`，支持操作日志和登录日志查询
- 数据字典：`/api/sys/dict`，支持通用枚举配置

### 3. 客户管理接口
- 客户信息：`/api/customer`，支持客户增删改查、导入导出
- 联系人管理：`/api/contact`，支持联系人增删改查
- 跟进记录：`/api/follow`，支持客户跟进记录管理

### 4. 销售管理接口
- 商机管理：`/api/opportunity`，支持商机增删改查、阶段更新
- 合同管理：`/api/contract`，支持合同增删改查、附件上传
- 订单管理：`/api/order`，支持订单增删改查、状态更新
- 回款管理：`/api/payment`，支持回款增删改查、状态更新

### 5. 数据统计接口
- 客户统计：`/api/stats/customer`，支持按时间、分类、部门统计
- 销售统计：`/api/stats/sales`，支持按员工、部门、时间统计
- 商机统计：`/api/stats/opportunity`，支持按阶段、时间统计
- 回款统计：`/api/stats/payment`，支持按时间、方式统计
- 仪表盘数据：`/api/stats/dashboard`，支持首页核心数据展示

## 六、项目结构

### 1. 后端项目结构
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/crm/
│   │   │   ├── aspect/         # 切面类
│   │   │   ├── common/         # 公共组件
│   │   │   ├── config/         # 配置类
│   │   │   ├── controller/     # 控制器
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── entity/         # 实体类
│   │   │   ├── exception/      # 异常处理
│   │   │   ├── filter/         # 过滤器
│   │   │   ├── handler/        # 处理器
│   │   │   ├── mapper/         # 数据访问层
│   │   │   ├── service/        # 业务层
│   │   │   ├── utils/          # 工具类
│   │   │   ├── vo/             # 视图对象
│   │   │   └── CrmApplication.java  # 主应用类
│   │   └── resources/
│   │       ├── mapper/         # XML映射文件
│   │       └── application.yml # 应用配置
│   └── test/                   # 测试代码
└── pom.xml                     # Maven依赖
```

### 2. 前端项目结构
```
frontend/
├── src/
│   ├── api/                    # 接口请求封装
│   │   ├── __tests__/          # API测试
│   │   ├── customer/           # 客户相关接口
│   │   ├── sales/              # 销售相关接口
│   │   └── system/             # 系统相关接口
│   ├── config/                 # 配置文件
│   ├── layout/                 # 布局组件
│   ├── pages/                  # 页面组件
│   │   ├── customer/           # 客户管理
│   │   ├── dashboard/          # 仪表盘
│   │   ├── login/              # 登录页面
│   │   ├── sales/              # 销售管理
│   │   │   ├── contract/       # 合同管理
│   │   │   ├── opportunity/    # 商机管理
│   │   │   ├── order/          # 订单管理
│   │   │   └── payment/         # 回款管理
│   │   ├── stats/              # 数据统计
│   │   └── system/             # 系统管理
│   │       ├── dept/            # 部门管理
│   │       ├── dict/            # 字典管理
│   │       ├── log/             # 日志管理
│   │       ├── menu/            # 菜单管理
│   │       ├── role/            # 角色管理
│   │       └── user/            # 用户管理
│   ├── router/                 # 路由配置
│   ├── store/                  # 状态管理
│   ├── tests/                  # 测试代码
│   ├── App.vue                 # 根组件
│   └── main.js                 # 入口文件
├── tests/                      # 测试配置
├── .env.development            # 开发环境配置
├── .env.production             # 生产环境配置
├── index.html                  # HTML模板
├── package.json                # 项目配置
└── vite.config.js              # Vite配置
```

## 七、核心功能亮点

### 1. 可扩展性设计
- 模块化架构，各功能模块独立开发和部署
- 基于Spring Boot的自动配置，支持环境变量和配置文件分离
- 前端组件化开发，支持组件复用和扩展

### 2. 安全性设计
- 基于JWT的无状态认证，支持token刷新
- 基于Spring Security的细粒度权限控制，支持菜单权限和按钮权限
- 密码BCrypt加密存储，禁止明文传输
- 接口参数校验，防止XSS攻击和SQL注入

### 3. 性能优化点
- 使用MyBatis-Plus的缓存机制，减少数据库查询
- 分页查询使用MySQL的LIMIT语句，避免全表扫描
- 前端使用Vue3的Composition API，减少组件重渲染
- 图表数据使用ECharts的按需加载，提升页面加载速度

## 八、常见问题排查

### 1. 后端启动失败
- 检查数据库连接配置是否正确
- 检查端口是否被占用
- 检查依赖是否完整安装

### 2. 前端启动失败
- 检查Node.js版本是否符合要求
- 检查npm/yarn依赖是否完整安装
- 检查端口是否被占用

### 3. 登录失败
- 检查用户名和密码是否正确
- 检查数据库中用户状态是否启用
- 检查JWT密钥配置是否正确

### 4. 接口调用失败
- 检查前端API地址配置是否正确
- 检查后端服务是否正常运行
- 检查用户权限是否足够

### 5. 数据导入导出失败
- 检查Excel文件格式是否正确
- 检查导入数据是否符合校验规则
- 检查服务器临时目录权限是否正确

## 九、技术支持

如有任何问题，请联系技术支持团队。

- 邮箱：support@crm-system.com
- 电话：400-123-4567
- 文档：https://crm-system.com/docs

## 十、更新日志

### v1.1
- 更新项目结构，完善前端和后端目录组织
- 优化数据库初始化脚本，添加更多基础数据
- 修复编译错误，确保项目能够正常运行
- 完善文档说明，更新版本号

---

**注意**：本项目为演示版本，生产环境部署时请修改相关配置参数，确保系统安全性。