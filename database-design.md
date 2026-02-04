# CRM系统数据库设计

## 一、数据库基本信息

- 数据库：MySQL 8.0
- 字符集：utf8mb4
- 排序规则：utf8mb4_unicode_ci

## 二、系统管理模块表结构

### 1. 部门表（sys_dept）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 部门ID |
| dept_name | varchar | 50 | 是 | 否 | 部门名称 |
| parent_id | bigint | 20 | 是 | 否 | 父部门ID |
| order_num | int | 11 | 是 | 否 | 排序号 |
| status | char | 1 | 是 | 否 | 状态（0-禁用，1-启用） |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 2. 角色表（sys_role）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 角色ID |
| role_name | varchar | 50 | 是 | 否 | 角色名称 |
| role_key | varchar | 50 | 是 | 否 | 角色关键字 |
| order_num | int | 11 | 是 | 否 | 排序号 |
| status | char | 1 | 是 | 否 | 状态（0-禁用，1-启用） |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 3. 用户表（sys_user）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 用户ID |
| username | varchar | 50 | 是 | 否 | 用户名 |
| password | varchar | 100 | 是 | 否 | 密码（BCrypt加密） |
| nickname | varchar | 50 | 是 | 否 | 昵称 |
| dept_id | bigint | 20 | 否 | 否 | 部门ID |
| email | varchar | 100 | 否 | 否 | 邮箱 |
| phone | varchar | 20 | 否 | 否 | 手机号 |
| avatar | varchar | 255 | 否 | 否 | 头像 |
| status | char | 1 | 是 | 否 | 状态（0-禁用，1-启用） |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 4. 菜单表（sys_menu）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 菜单ID |
| menu_name | varchar | 50 | 是 | 否 | 菜单名称 |
| parent_id | bigint | 20 | 是 | 否 | 父菜单ID |
| order_num | int | 11 | 是 | 否 | 排序号 |
| path | varchar | 200 | 否 | 否 | 路由路径 |
| component | varchar | 255 | 否 | 否 | 组件路径 |
| query | varchar | 255 | 否 | 否 | 路由参数 |
| is_frame | char | 1 | 是 | 否 | 是否为外部链接（0-否，1-是） |
| is_cache | char | 1 | 是 | 否 | 是否缓存（0-否，1-是） |
| menu_type | char | 1 | 是 | 否 | 菜单类型（M-目录，C-菜单，F-按钮） |
| visible | char | 1 | 是 | 否 | 菜单状态（0-禁用，1-启用） |
| status | char | 1 | 是 | 否 | 菜单状态（0-禁用，1-启用） |
| perms | varchar | 100 | 否 | 否 | 权限标识 |
| icon | varchar | 100 | 否 | 否 | 菜单图标 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 5. 角色菜单关联表（sys_role_menu）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | ID |
| role_id | bigint | 20 | 是 | 否 | 角色ID |
| menu_id | bigint | 20 | 是 | 否 | 菜单ID |

### 6. 用户角色关联表（sys_user_role）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | ID |
| user_id | bigint | 20 | 是 | 否 | 用户ID |
| role_id | bigint | 20 | 是 | 否 | 角色ID |

### 7. 操作日志表（sys_oper_log）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 日志ID |
| title | varchar | 100 | 是 | 否 | 操作标题 |
| business_type | int | 2 | 是 | 否 | 业务类型（0-其他，1-新增，2-修改，3-删除） |
| method | varchar | 100 | 否 | 否 | 方法名称 |
| request_method | varchar | 10 | 否 | 否 | 请求方式 |
| operator_type | int | 1 | 是 | 否 | 操作类型（0-其他，1-后台用户，2-手机端用户） |
| oper_name | varchar | 50 | 否 | 否 | 操作人员 |
| dept_name | varchar | 50 | 否 | 否 | 部门名称 |
| oper_url | varchar | 255 | 否 | 否 | 请求URL |
| oper_ip | varchar | 128 | 否 | 否 | 操作IP |
| oper_location | varchar | 255 | 否 | 否 | 操作地点 |
| oper_param | varchar | 2000 | 否 | 否 | 请求参数 |
| json_result | varchar | 2000 | 否 | 否 | 返回参数 |
| status | int | 1 | 是 | 否 | 操作状态（0-失败，1-成功） |
| error_msg | varchar | 2000 | 否 | 否 | 错误消息 |
| oper_time | datetime | - | 是 | 否 | 操作时间 |

### 8. 登录日志表（sys_login_log）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 日志ID |
| username | varchar | 50 | 是 | 否 | 用户名 |
| status | char | 1 | 是 | 否 | 登录状态（0-失败，1-成功） |
| ipaddr | varchar | 128 | 否 | 否 | IP地址 |
| login_location | varchar | 255 | 否 | 否 | 登录地点 |
| browser | varchar | 50 | 否 | 否 | 浏览器类型 |
| os | varchar | 50 | 否 | 否 | 操作系统 |
| msg | varchar | 255 | 否 | 否 | 提示消息 |
| login_time | datetime | - | 是 | 否 | 登录时间 |

### 9. 数据字典表（sys_dict）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 字典ID |
| dict_name | varchar | 100 | 是 | 否 | 字典名称 |
| dict_type | varchar | 100 | 是 | 否 | 字典类型 |
| status | char | 1 | 是 | 否 | 状态（0-禁用，1-启用） |
| order_num | int | 11 | 是 | 否 | 排序号 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 10. 数据字典详情表（sys_dict_item）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 字典项ID |
| dict_id | bigint | 20 | 是 | 否 | 字典ID |
| item_name | varchar | 100 | 是 | 否 | 字典项名称 |
| item_value | varchar | 100 | 是 | 否 | 字典项值 |
| status | char | 1 | 是 | 否 | 状态（0-禁用，1-启用） |
| order_num | int | 11 | 是 | 否 | 排序号 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

## 三、客户管理模块表结构

### 1. 客户表（crm_customer）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 客户ID |
| customer_name | varchar | 100 | 是 | 否 | 客户名称 |
| customer_type | varchar | 50 | 否 | 否 | 客户类型（潜在客户、意向客户、合作客户、流失客户） |
| industry | varchar | 50 | 否 | 否 | 所属行业 |
| address | varchar | 255 | 否 | 否 | 地址 |
| phone | varchar | 20 | 否 | 否 | 电话 |
| email | varchar | 100 | 否 | 否 | 邮箱 |
| website | varchar | 255 | 否 | 否 | 网站 |
| source | varchar | 50 | 否 | 否 | 客户来源 |
| owner_user_id | bigint | 20 | 否 | 否 | 负责人ID |
| owner_user_name | varchar | 50 | 否 | 否 | 负责人名称 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 2. 联系人表（crm_contact）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 联系人ID |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| contact_name | varchar | 50 | 是 | 否 | 联系人姓名 |
| position | varchar | 50 | 否 | 否 | 职位 |
| phone | varchar | 20 | 否 | 否 | 手机号 |
| email | varchar | 100 | 否 | 否 | 邮箱 |
| priority | int | 1 | 否 | 否 | 优先级（1-高，2-中，3-低） |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 3. 客户跟进记录表（crm_follow_record）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 记录ID |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| follow_content | text | - | 否 | 否 | 跟进内容 |
| follow_way | varchar | 50 | 否 | 否 | 跟进方式（电话、微信、面谈） |
| follow_time | datetime | - | 是 | 否 | 跟进时间 |
| next_follow_time | datetime | - | 否 | 否 | 下次跟进时间 |
| follow_user_id | bigint | 20 | 否 | 否 | 跟进人ID |
| follow_user_name | varchar | 50 | 否 | 否 | 跟进人名称 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 4. 客户流失表（crm_customer_loss）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 流失ID |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| loss_reason | text | - | 否 | 否 | 流失原因 |
| loss_time | datetime | - | 是 | 否 | 流失时间 |
| handle_status | varchar | 50 | 否 | 否 | 处理状态（待处理、已处理） |
| handle_remark | text | - | 否 | 否 | 处理备注 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

## 四、销售管理模块表结构

### 1. 商机表（crm_business）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 商机ID |
| business_name | varchar | 100 | 是 | 否 | 商机名称 |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| business_amount | decimal | 18,2 | 否 | 否 | 商机金额 |
| business_stage | varchar | 50 | 否 | 否 | 商机阶段（初步接洽、需求确认、方案报价、合同签订） |
| expected_close_time | datetime | - | 否 | 否 | 预计成交时间 |
| probability | int | 3 | 否 | 否 | 成交概率（0-100） |
| owner_user_id | bigint | 20 | 否 | 否 | 负责人ID |
| owner_user_name | varchar | 50 | 否 | 否 | 负责人名称 |
| status | varchar | 50 | 否 | 否 | 状态（进行中、已关闭、已转化） |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 2. 合同表（crm_contract）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 合同ID |
| contract_name | varchar | 100 | 是 | 否 | 合同名称 |
| contract_no | varchar | 50 | 是 | 否 | 合同编号 |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| business_id | bigint | 20 | 否 | 否 | 商机ID |
| contract_amount | decimal | 18,2 | 否 | 否 | 合同金额 |
| start_time | datetime | - | 否 | 否 | 开始时间 |
| end_time | datetime | - | 否 | 否 | 结束时间 |
| contract_status | varchar | 50 | 否 | 否 | 合同状态（草稿、生效、终止、过期） |
| attachment | varchar | 255 | 否 | 否 | 附件路径 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 3. 订单表（crm_order）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 订单ID |
| order_no | varchar | 50 | 是 | 否 | 订单编号 |
| contract_id | bigint | 20 | 是 | 否 | 合同ID |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| order_amount | decimal | 18,2 | 否 | 否 | 订单金额 |
| order_status | varchar | 50 | 否 | 否 | 订单状态（待付款、已付款、待发货、已完成、已取消） |
| create_time | datetime | - | 是 | 否 | 订单时间 |
| pay_time | datetime | - | 否 | 否 | 付款时间 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

### 4. 回款表（crm_payment）

| 字段名 | 字段类型 | 长度 | 是否非空 | 主键 | 注释 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | bigint | 20 | 是 | 是 | 回款ID |
| payment_no | varchar | 50 | 是 | 否 | 回款编号 |
| order_id | bigint | 20 | 是 | 否 | 订单ID |
| customer_id | bigint | 20 | 是 | 否 | 客户ID |
| payment_amount | decimal | 18,2 | 否 | 否 | 回款金额 |
| payment_time | datetime | - | 是 | 否 | 回款时间 |
| payment_way | varchar | 50 | 否 | 否 | 回款方式 |
| create_by | varchar | 64 | 是 | 否 | 创建人 |
| create_time | datetime | - | 是 | 否 | 创建时间 |
| update_by | varchar | 64 | 是 | 否 | 更新人 |
| update_time | datetime | - | 是 | 否 | 更新时间 |
| del_flag | char | 1 | 是 | 否 | 逻辑删除标记（0-未删除，1-已删除） |
| remark | varchar | 500 | 否 | 否 | 备注 |

## 五、表关联关系

1. **系统管理模块**：
   - 用户与角色：多对多，通过sys_user_role表关联
   - 角色与菜单：多对多，通过sys_role_menu表关联
   - 用户与部门：多对一，用户表的dept_id关联部门表的id

2. **客户管理模块**：
   - 客户与联系人：一对多，联系人表的customer_id关联客户表的id
   - 客户与跟进记录：一对多，跟进记录表的customer_id关联客户表的id
   - 客户与流失记录：一对多，流失记录表的customer_id关联客户表的id

3. **销售管理模块**：
   - 客户与商机：一对多，商机表的customer_id关联客户表的id
   - 商机与合同：一对一或一对多，合同表的business_id关联商机表的id
   - 合同与订单：一对多，订单表的contract_id关联合同表的id
   - 订单与回款：一对多，回款表的order_id关联订单表的id
   - 客户与合同：一对多，合同表的customer_id关联客户表的id
   - 客户与订单：一对多，订单表的customer_id关联客户表的id
   - 客户与回款：一对多，回款表的customer_id关联客户表的id

## 六、设计规范

1. **命名规范**：
   - 表名：使用小写字母，单词之间用下划线分隔，前缀标识模块（如sys_、crm_）
   - 字段名：使用小写字母，单词之间用下划线分隔
   - 主键：统一使用id，类型为bigint

2. **通用字段**：
   - 所有表都包含通用字段：id、create_by、create_time、update_by、update_time、del_flag、remark
   - del_flag：逻辑删除标记，0-未删除，1-已删除

3. **数据类型**：
   - 字符串：使用varchar，根据实际需要设置长度
   - 文本：使用text
   - 整数：使用int或bigint
   - 小数：使用decimal，如金额使用decimal(18,2)
   - 日期时间：使用datetime
   - 状态：使用char(1)或varchar

4. **索引**：
   - 主键自动创建索引
   - 外键字段建议创建索引
   - 经常用于查询的字段建议创建索引

5. **约束**：
   - 非空约束：根据业务需要设置
   - 唯一约束：如用户名、字典类型等
   - 外键约束：根据业务需要设置，确保数据一致性

## 七、扩展性考虑

1. **预留字段**：
   - 部分表预留了remark字段，可用于存储额外信息
   - 可根据业务发展需要，在现有表中添加新字段

2. **模块扩展**：
   - 系统设计采用模块化架构，可方便添加新模块
   - 如后续需要添加产品管理模块，可新增相关表结构

3. **数据字典**：
   - 使用数据字典管理系统中的枚举值，如客户类型、商机阶段等
   - 可通过数据字典动态配置，无需修改代码

4. **权限控制**：
   - 基于菜单和按钮的权限控制，可根据业务需要灵活配置
   - 支持角色的批量授权，方便权限管理

## 八、性能优化

1. **索引优化**：
   - 为经常查询的字段创建索引
   - 避免在大字段上创建索引

2. **查询优化**：
   - 使用分页查询，避免一次性查询大量数据
   - 合理使用关联查询，避免过度关联

3. **存储优化**：
   - 合理设置字段类型和长度
   - 定期清理无效数据

4. **缓存优化**：
   - 对于频繁访问的数据，可考虑使用缓存
   - 如数据字典、菜单权限等

## 九、安全性考虑

1. **密码安全**：
   - 用户密码使用BCrypt加密存储
   - 禁止明文存储密码

2. **数据安全**：
   - 敏感数据加密存储
   - 定期备份数据库

3. **访问安全**：
   - 基于角色的权限控制
   - 接口请求参数校验
   - 防止SQL注入、XSS攻击等

4. **审计日志**：
   - 记录用户操作日志
   - 记录登录日志，便于追溯