# 古茗奶茶小程序后端服务

本项目是仿古茗奶茶小程序的后端服务，基于Spring Boot + Spring Cloud + MyBatis-Plus开发。

## 项目结构

```
goodme-backend
├── goodme-api         # API接口模块，包含控制器和启动类
├── goodme-common      # 公共模块，包含通用工具类、常量、枚举等
├── goodme-framework   # 框架模块，包含核心配置、安全框架、数据库操作等
├── goodme-service     # 业务服务模块，包含各业务领域的服务实现
└── sql                # 数据库脚本
```

## 技术栈

- **核心框架**：Spring Boot 2.7.x / Spring Cloud 2021.x
- **ORM框架**：MyBatis-Plus 3.5.3
- **数据库**：MySQL 8.0
- **缓存**：Redis 6.x
- **安全框架**：Spring Security + JWT
- **接口文档**：Knife4j (基于Swagger)
- **连接池**：Druid
- **分布式锁**：Redisson
- **其他工具**：Hutool、MapStruct、Lombok等

## 功能模块

- 用户认证模块
- 商品模块
- 购物车模块
- 订单模块
- 会员与积分模块
- 优惠券模块
- 店铺模块
- 评价模块
- 地址管理模块

## 开发环境

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

## 快速开始

1. 克隆项目到本地
2. 创建数据库goodme，并执行sql/goodme.sql脚本
3. 修改application-dev.yml中的数据库和Redis配置
4. 启动项目：运行GoodmeApplication.java

## API文档

启动项目后，访问：http://localhost:8080/api/doc.html 