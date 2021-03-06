# Sharding-Sphere 分库分表相关参考
[![](https://img.shields.io/badge/Author-Killah-orange.svg)](https://github.com/killahs)
[![](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)](https://github.com/killahs/killah-plus-full/tree/main/wechat-pay)
[![GitHub stars](https://img.shields.io/github/stars/Killahs/killah-plus-full.svg?style=social&label=Stars)](https://github.com/killahs/killah-plus-full)
[![GitHub forks](https://img.shields.io/github/forks/Killahs/killah-plus-full.svg?style=social&label=Fork)](https://github.com/killahs/killah-plus-full)

## 一、Sharding Sphere实现分库
有关分库分表、Sharding Sphere 的理论以及知识点下面简单介绍：
- [1. 分库分表理论](https://github.com/killahs/killah-plus-full/blob/main/sharding-sphere/blog/README-01.md)
- [2. Sharding Sphere](https://github.com/killahs/killah-plus-full/blob/main/sharding-sphere/blog/README-02.md)
- [3. SpringBoot + Sharding Sphere 实现读写分离](https://github.com/killahs/killah-plus-full/tree/main/sharding-sphere/db-read-write)
- [4. SpringBoot + Sharding Sphere 实现分表](https://github.com/killahs/killah-plus-full/tree/main/sharding-sphere/sub-table)
- [5. SpringBoot + Sharding Sphere 实现分库分表](https://github.com/killahs/killah-plus-full/tree/main/sharding-sphere/sub-db-table)
- [6. SpringBoot + Sharding Sphere 实现分表 + 读写分离](https://github.com/killahs/killah-plus-full/tree/main/sharding-sphere/sub-table-read-write)
- [7. SpringBoot + Sharding Sphere 实现分库分表 + 读写分离](https://github.com/killahs/killah-plus-full/tree/main/sharding-sphere/sub-db-table-read-write)

## 二、项目概述
### 2.1. 技术架构
项目总体技术选型：
- [x] Spring Boot
- [x] Sharding Sphere
- [x] Maven
- [x] MySQL
- [x] Lombok（插件）

### 2.2. 项目说明

**场景:** 在实际开发中，如果数据库压力大我们可以通过 分库分表 的基础上进行 读写分离，来减缓数据库压力。

### 2.3. 项目整体结构
```
sharding-sphere # 父工程
  | 
  |---blog            #理论知识
  | 
  |---db-read-write   #实现读写分离功能
  | 
  |---sub-table       #实现分表功能
  | 
  |---sub-db-table    #实现分库分表功能                         
  | 
  |---sub-table-read-write     #实现分表 + 读写分离                                        
  | 
  |---sub-db-table-read-write  #实现分库分表 + 读写分离 
```

### 2.4. 技术相关依赖
```xml
<properties>
    <java.version>1.8</java.version>
    <mybatis-spring-boot>2.0.1</mybatis-spring-boot>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>${mybatis-spring-boot}</version>
    </dependency>
    <!--MyBatis 驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!--Druid 数据源-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.16</version>
    </dependency>
    <!--Sharding Sphere 最新版本-->
    <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
        <version>4.0.0-RC1</version>
    </dependency>
    <!--Lombok 实体工具-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
