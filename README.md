# 数据库表导出到文件
## 工程描述
数据库表数据异步导出到文件。
* 支持多节点部署
* 支持动态数据源切换
* 支持导出格式 [ TXT | ZIP ]
## 依赖
* 环境变量配置 jdk-17
* 本地已安装 gitbash

## 安装
* 使用 gitbash 进入工程目录
```shell
# 安装通用组件
bash common/scripts/install.sh
```

## 配置数据源
* 核心数据源
```shell
# 安装通用组件
cat bootstrap/src/main/resources/application.yml
```
* 扩展数据源
```shell
# 安装通用组件
cat service/service-dbexpt/src/main/resources/datasource.properties
```

## 数据库初始化脚本
```shell
# 安装通用组件
cat service/service-dbexpt/src/main/resources/scripts/schema.sql
```

## 工程入口
```shell
# 安装通用组件
cat bootstrap/src/main/java/space/caoshd/AppBoot.java
```

## 待优化
* 数据库表导出到文件 任务生成组件
