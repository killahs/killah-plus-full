# Sharding-Sphere 分库分表相关参考
[![](https://img.shields.io/badge/Author-Killah-orange.svg)](https://github.com/killahs)
[![](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)](https://github.com/killahs/killah-plus-full/tree/main/wechat-pay)
[![GitHub stars](https://img.shields.io/github/stars/Killahs/killah-plus-full.svg?style=social&label=Stars)](https://github.com/killahs/killah-plus-full)
[![GitHub forks](https://img.shields.io/github/forks/Killahs/killah-plus-full.svg?style=social&label=Fork)](https://github.com/killahs/killah-plus-full)

## 一、Sharding Sphere实现分库
有关分库分表、Sharding Sphere 的理论以及知识点下面简单介绍：
- [1. 分库分表理论](https://github.com/killahs/killah-plus-full/blob/main/sharding-sphere/blog/README-01.md)
- [2. Sharding Sphere](https://github.com/killahs/killah-plus-full/blob/main/sharding-sphere/blog/README-02.md)

## 二、项目概述
### 2.1. 技术架构
项目总体技术选型：
- [x] Spring Boot
- [x] Sharding Sphere
- [x] Maven
- [x] MySQL
- [x] Lombok(插件)

### 2.2. 项目说明

**场景:** 在实际开发中，如果数据库压力大我们可以通过 分库分表 的基础上进行 读写分离，来减缓数据库压力。

### 2.3. 项目整体结构
```
sharding-sphere # 父工程
  | 
  |---blog            #理论只是
  | 
  |---db-read-write   #实现读写分离功能
  | 
  |---sub-table       #实现分表功能
  | 
  |---sub-db-table    #实现分库分表功能                         
  | 
  |---sub-table-read-write    #实现分表 + 读写分离                                        
  | 
  |---sub-db-table-read-write #实现分库分表 + 读写分离 
  
```