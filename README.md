**Bank Transaction System**
==========================

项目描述
--------

这是一个基于 Spring Boot 的银行交易管理系统，用于记录、查看和管理财务交易。系统支持创建、修改、删除和查询交易，所有数据存储在内存中（使用 H2 数据库）。

技术栈
--------

* Java 21
* Spring Boot 3.x
* H2 Database（内存数据库）
* Spring Data JPA（数据访问层）
* Lombok（减少样板代码）
* Spring Cache Abstraction（缓存支持）
* Docker（容器化部署）

功能列表
------------

* 创建交易：支持存款、取款、转账等交易类型。
* 查询交易：支持分页查询所有交易。
* 删除交易：根据交易 ID 删除交易。
* 修改交易：更新交易的详细信息。
* 缓存机制：使用 Caffeine 缓存提高查询性能。
* 异常处理：处理无效交易、重复交易等异常情况。

快速开始
------------

环境要求
------------

* Java 21
* Maven 3.x
* Docker（可选，用于容器化部署）

克隆项目
------------

```bash
git clone https://github.com/footman101/bank-transaction-system.git
cd bank-transaction-system
```

运行项目
------------

使用 Maven：
```bash
mvn spring-boot:run
```
应用将启动在 http://localhost:8080

使用 Docker：
```bash
docker build -t bank-transaction-system .
docker run -p 8080:8080 bank-transaction-system
```

API 文档
------------

### 1. 创建交易

* URL: POST /api/transactions
* 请求体:
```json
{
  "type": "DEPOSIT",
  "amount": 100.0,
  "sourceAccount": "123456789",
  "targetAccount": "987654321"
}
```
* 响应:
```json
{
  "id": 1,
  "type": "DEPOSIT",
  "amount": 100.0,
  "sourceAccount": "123456789",
  "targetAccount": "987654321",
  "timestamp": "2023-10-01T12:00:00",
  "status": "PENDING"
}
```

### 2. 查询所有交易

* URL: GET /api/transactions?page=0&size=10
* 响应:
```json
{
  "content": [
    {
      "id": 1,
      "type": "DEPOSIT",
      "amount": 100.0,
      "sourceAccount": "123456789",
      "targetAccount": "987654321",
      "timestamp": "2023-10-01T12:00:00",
      "status": "PENDING"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 1,
  "totalElements": 1
}
```

### 3. 删除交易

* URL: DELETE /api/transactions/{id}
* 响应：HTTP 204 No Content

### 4. 修改交易

* URL: PUT /api/transactions/{id}
* 请求体:
```json
{
  "type": "WITHDRAWAL",
  "amount": 50.0,
  "sourceAccount": "123456789",
  "targetAccount": null
}
```
* 响应:
```json
{
  "id": 1,
  "type": "WITHDRAWAL",
  "amount": 50.0,
  "sourceAccount": "123456789",
  "targetAccount": null,
  "timestamp": "2023-10-01T12:00:00",
  "status": "PENDING"
}
```

测试
-----

### 单元测试

运行以下命令执行单元测试：
```bash
mvn test
```

### 压力测试
使用 JMeter 进行压力测试：

创建 500 个并发请求，测试 /api/transactions 的响应时间和吞吐量。

可以直接使用`perf/JMeter_perf.jmx` 导入到 JMeter 进行压力测试

本地测试结果：`perf/create.png` `perf/query.png`

容器化部署
-------------

### 构建 Docker 镜像

```bash
docker build -t bank-transaction-system .
```

### 运行容器

```bash
docker run -p 8080:8080 bank-transaction-system
```

依赖项说明
-------------

* Spring Web：提供 RESTful API 支持
* Spring Data JPA：简化数据库操作
* H2 Database：内存数据库，用于开发和测试
* Lombok：自动生成 getter/setter，减少样板代码
* Spring Cache Abstraction：提供缓存支持
* Validation：实现请求参数的自动化验证

项目结构
-------------

```bash
bank-transaction-system/
├── src/
│   ├── main/
│   │   ├── java/com/example/bank/
│   │   │   ├── controller/          # 控制器层
│   │   │   ├── service/             # 服务层
│   │   │   ├── repository/          # 数据访问层
│   │   │   ├── model/               # 实体类
│   │   │   └── exception/           # 异常处理
│   │   └── resources/               # 配置文件
│   └── test/                        # 测试代码
├── Dockerfile                       # Docker 配置文件
├── pom.xml                          # Maven 配置文件
└── README.md                        # 项目文档