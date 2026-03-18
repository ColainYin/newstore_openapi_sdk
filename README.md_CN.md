# NEWPOS STORE Open SDK（Java）

## 1. SDK 简介

NEWPOS STORE Open SDK（Java）用于帮助第三方系统快速、安全地接入 **NEWPOS STORE平台**，通过标准化接口获取终端及应用相关数据。

SDK 对接口调用、鉴权认证、参数封装及返回结果进行了统一处理，调用方无需关注底层通信和安全细节，即可完成业务对接。

## 2. SDK 功能概览

当前 SDK 提供以下能力：

- 终端基础信息查询

- 终端已安装应用列表查询

- 分页数据返回封装

- API Key / API Secret 鉴权机制

- 统一返回对象结构

  

##  3. 运行环境要求

- **JDK：11 及以上**

- Maven 或 Gradle 构建工具

- 网络可访问 NEWPOS STORE 服务地址

  

## 4. 接入流程说明

整体接入流程如下：

1. 向 NEWPOS STORE 平台申请 Open API 接入权限

   **https://www.newposstore.com/**

   **注册NEWPOS STORE云平台之后申请开发者&设备管理权限即可开通Open Api权限**

   ![](./img/image-20260115144859578.png)

   ![](./img\image-20260115144948309.png)

   

2. 获取平台分配的 `API Key` 与 `API Secret`

3. 引入 Open SDK

4. 初始化 SDK 客户端

5. 根据业务需求调用对应接口

## 5. SDK 初始化

在初始化 SDK 客户端之前，请确保已将 SDK JAR 包正确安装到本地 Maven 仓库中。

### 5.1 将 SDK JAR 安装到本地 Maven 仓库

如果 SDK 以 **独立 JAR 包形式** 提供（未发布到公共 Maven 仓库），需要手动将该 JAR 安装到本地 Maven 仓库。

#### 步骤一：准备 SDK JAR 包

确认已从 NEWPOS STORE 获取 SDK JAR 文件，例如：

```
newstore-openapi-sdk-1.0.0.jar
```

#### 步骤二：使用 Maven 命令安装 JAR

在 JAR 文件所在目录执行以下命令：

```
mvn install:install-file -Dfile=newstore-openapi-sdk-1.0.0.jar -DgroupId=com.newposstore.api -DartifactId=newstore-openapi-sdk -Dversion=1.0.0 -Dpackaging=jar
```

命令执行成功后，SDK 将被安装到本地 Maven 仓库中。

------

### 5.2 引入 SDK 依赖

在项目的 `pom.xml` 中添加以下依赖：

```
<dependency>
    <groupId>com.newposstore.api</groupId>
    <artifactId>newstore-openapi-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

------

### 5.3 初始化 SDK 客户端

```

TermApi termApi = new TermApi(
    serverUrl,
    apiKey,
    apiSecret
);
```

------

### 注意事项

- 请确保本地已正确安装并配置 Maven 环境
- 若企业内部使用私有 Maven 仓库（如 Nexus / Artifactory），也可将 SDK JAR 上传至私服进行统一管理
- `groupId`、`artifactId`、`version` 请以 SDK 实际交付信息为准

## 6. 接口使用示例

### 6.1 查询终端已安装应用列表

用于分页查询指定终端当前已安装的应用信息。

#### 示例代码

```java
TermInstallAppListReq req = new TermInstallAppListReq();
req.setSn("921000010191");
req.setPageNum(1);
req.setPageSize(10);

TableDataInfo<TermAppInfResp> result = termApi.installedAppListCall(req);
```

#### 请求参数说明

| 参数名   | 类型    | 必填 | 说明           |
| -------- | ------- | ---- | -------------- |
| sn       | String  | 是   | 终端 SN        |
| pageNum  | Integer | 否   | 页码（默认 1） |
| pageSize | Integer | 否   | 每页条数       |

#### 返回结果说明

返回结果为分页数据结构：

```

TableDataInfo<T>
```

包含总数、当前页数据列表等信息。

------

### 6.2 查询终端详情

用于获取指定终端的基础信息。

#### 示例代码

```java

TermDetailReq req = new TermDetailReq();
req.setSn("921000010191");

R<TerminalDetailResp> resp = termApi.termDetailCall(req);
```

#### 返回结构说明

大部分接口统一返回：

```java
R<T>
```

## 7. 注意事项

- 请确保使用正确的服务地址（测试环境 / 生产环境）
- 建议对接口调用异常进行统一捕获处理
- 分页接口请合理控制 `pageSize`，避免一次性获取过多数据

------



## 8. 版本记录

### v1.0.0

- 初始版本
- 支持终端信息与应用列表查询

------



## 9. 技术支持

如在接入过程中遇到问题，请联系 NEWPOS STORE 技术支持团队获取帮助。



