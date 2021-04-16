# SpringBoot + Sharding Sphere 实现分表 + 读写分离
## 一、项目概述
### 1.1. 项目说明
**场景：** 在实际开发中，如果表的数据过大，我们可能需要把一张表拆分成多张表，这里就是通过 Sharding Sphere 实现分表 + 读写分离功能，但不分库。

### 1.2. 数据库设计
**分表：** tab_user单表拆分为 tab_user0 表和 tab_user1 表。

**读写分离：** 数据写入 master 库 ,数据读取 slave 库 。

**Master库**
![img34.png](../blog/image/img34.png)

**Slave库**
![img35.png](../blog/image/img35.png)

**说明：** 初始数据的时候，这边只有 slave 从库的tab_user0 我插入了一条数据。那是因为我们这个项目中 MySql 服务器并没有实现主从部署，这两个库都在同一服务器上。 做不到主数据库数据自动同步到从数据库，所以这里在从数据库建一条数据。等下验证的时候，我们只需验证数据是否存入master库，数据读取是否在slave库。

## 二、核心代码

**说明：** 这里只贴出与技术相关的代码，完整代码请参考项目。

### 2.1. application.properties
```properties
server.port=8084

#指定mybatis信息
mybatis.config-location=classpath:mybatis-config.xml

#数据库
spring.shardingsphere.datasource.names=master0,slave0

spring.shardingsphere.datasource.master0.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.master0.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.master0.url=jdbc:mysql://localhost:3306/master?characterEncoding=utf-8
spring.shardingsphere.datasource.master0.username=root
spring.shardingsphere.datasource.master0.password=123456

spring.shardingsphere.datasource.slave0.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.slave0.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.slave0.url=jdbc:mysql://localhost:3306/slave?characterEncoding=utf-8
spring.shardingsphere.datasource.slave0.username=root
spring.shardingsphere.datasource.slave0.password=root

#数据分表规则
#指定所需分的表
spring.shardingsphere.sharding.tables.tab_user.actual-data-nodes=master0.tab_user$->{0..1}
#指定主键
spring.shardingsphere.sharding.tables.tab_user.table-strategy.inline.sharding-column=id
#分表规则为主键除以2取模
spring.shardingsphere.sharding.tables.tab_user.table-strategy.inline.algorithm-expression=tab_user$->{id % 2}

# 读写分离
spring.shardingsphere.masterslave.load-balance-algorithm-type=round_robin
spring.shardingsphere.masterslave.name=ms
#这里配置读写分离的时候一定要记得添加主库的数据源名称 这里为master0
spring.shardingsphere.sharding.master-slave-rules.master0.master-data-source-name=master0
spring.shardingsphere.sharding.master-slave-rules.master0.slave-data-source-names=slave0

#打印sql
spring.shardingsphere.props.sql.show=true
```
Sharding-JDBC可以通过 Java、YAML、Spring命名空间和 Spring Boot Starter四种方式配置，开发者可根据场景选择适合的配置方式，具体可以看官网。


### 2.2. UserController
```java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("user/list")
    public Object listUser() {
        return userService.list();
    }

    /**
     * 批量保存用户
     */
    @PostMapping("user/save")
    public Object saveUser() {
        List<User> users = Lists.newArrayList();
        users.add(new User("小小", "女", 3));
        users.add(new User("大大", "男", 5));
        users.add(new User("爸爸", "男", 30));
        users.add(new User("妈妈", "女", 28));
        users.add(new User("爷爷", "男", 64));
        users.add(new User("奶奶", "女", 62));
        return userService.insertForeach(users);
    }
}
```

## 三、测试验证
### 3.1. 批量插入数据
**请求接口：**
`localhost:8086/user/save`

我们可以从商品接口代码中可以看出，它会批量插入5条数据。我们先看控制台输出SQL语句。

![img36.png](../blog/image/img36.png)

我们可以从SQL语句可以看出 master 数据源 中 tab_user0 表插入了三条数据，而 tab_user1 表中插入两条数据。

我们再来看数据库：

**tab_user0：**
![img37.png](../blog/image/img37.png)

**tab_user1：**
![img38.png](../blog/image/img38.png)

**tab_user2：**
![img39.png](../blog/image/img39.png)

### 3.2. 获取数据
我们来获取列表接口的SQL。

```MySQL
select *  from tab_user 
```
**请求接口结果：**

![img40.png](../blog/image/img40.png)

**结论：** 从接口返回的结果可以很明显的看出，数据存储在master主库,而数据库的读取在slave从库。

**注意：** ShardingSphere并不支持CASE WHEN、HAVING、UNION (ALL)，有限支持子查询。这个官网有详细说明。
