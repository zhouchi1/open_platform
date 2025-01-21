# open_platform 开源微服务商城系统

## 项目结构：

```bash
open_platform
├── address_service           # 地址服务
├── cart_service              # 购物车服务
├── common                    # 公共模块
├── email_service             # 邮箱服务
├── login_service             # 登录服务
├── order_service             # 订单服务
├── pay_service               # 支付服务
├── register_service          # 注册服务
├── sentinel_dashboard_service # Sentinel Dashboard 服务
├── warehouse_service         # 仓库服务
├── xxl-job-admin             # 定时任务服务
└── pom.xml                   # 根 POM 文件
```

## 项目采用的技术栈：

**1、模块之间采用 Springcloud OpenFeign HTTP + Alibaba Dubbo3 RPC的方式进行通讯。**

**2、服务发现与服务配置采用Alibaba Nacos，各模块的YAML配置信息均存放在Nacos中集中管理。**

**3、Alibaba Sentinel 限流服务非原生内存存放规则方式，集成Nacos持久化配置，支持双向操作修改并持久化。**

**4、消息队列采用RabbitMQ 用于削峰、异步、解耦，缓存服务采用Redis 用于减少DB压力。**

**5、数据库使用Mysql，伴随ORM框架采用Mybatis-plus 简化数据库操作步骤。**

**6、支付服务采用微信支付，定时任务使用Xxl-job 分布式协调处理任务。**

**7、网关采用Springcloud Gateway 集中管理服务入口，使用Oauth2支持单点登录。**

**8、第三方文件存储服务采用Alibaba OSS对象存储。**

**9、WebSocket前后端双向通讯服务 采用Netty作为底层通讯依赖构建 支持抗高并发。**

**10、项目开发前后端对接使用Swagger接口文档 提升开发效率。**

**11、分布式事务采用Alibaba Seata，用于解决分布式环境下事务的统一。**

**12、数据库兼容分库分表设计，使用中间件ShardingSphere兼容Mybatis-Plus进行分库分表。**
