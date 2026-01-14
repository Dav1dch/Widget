# Android Widget

一个功能完善的Android桌面小组件项目。

## 项目概述

本项目从简单的Hello World Widget开始，逐步扩展为一个功能丰富的Android Widget应用。

## 项目结构

```
AndroidWidget/
├── app/                              # 应用模块
│   ├── src/
│   │   └── main/
│   │       ├── java/                 # Kotlin源代码
│   │       │   └── com/example/androidwidget/
│   │       │       ├── MainActivity.kt
│   │       │       └── widget/       # Widget相关类
│   │       │           └── HelloWorldWidgetProvider.kt
│   │       ├── res/                  # 资源文件
│   │       │   ├── layout/           # 布局文件
│   │       │   ├── values/           # 值资源
│   │       │   ├── drawable/         # 图形资源
│   │       │   ├── xml/              # XML配置
│   │       │   └── mipmap-*/         # 图标资源
│   │       └── AndroidManifest.xml   # 应用清单
│   ├── build.gradle.kts             # 应用级构建配置
│   └── proguard-rules.pro           # ProGuard规则
├── build.gradle.kts                  # 项目级构建配置
├── settings.gradle.kts              # Gradle设置
├── gradle.properties                # Gradle属性
├── DEVELOPMENT_PLAN.md              # 开发规划
└── README.md                        # 项目说明
```

## 开发进度

### 当前阶段：阶段一 - 准备阶段（Hello World）

**已完成：**
- ✅ 创建Android项目结构
- ✅ 创建Widget Provider类
- ✅ 创建Widget布局文件
- ✅ 配置AndroidManifest.xml
- ✅ 创建Widget配置文件
- ✅ 添加基础资源文件
- ⏳ 测试在模拟器/真机上运行
- ⏳ 验证Widget可以添加到桌面

### 后续阶段

详见 [DEVELOPMENT_PLAN.md](DEVELOPMENT_PLAN.md)

## 快速开始

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.2

### 构建步骤

1. 克隆项目到本地
```bash
git clone <repository-url>
cd AndroidWidget
```

2. 使用Android Studio打开项目

3. 同步Gradle
```
File -> Sync Project with Gradle Files
```

4. 构建项目
```
Build -> Make Project
```

### 运行应用

1. 连接Android设备或启动模拟器
2. 点击Run按钮或使用快捷键 `Shift + F10`
3. 应用将安装到设备上

### 添加Widget到桌面

1. 长按桌面空白区域
2. 选择"小组件"或"Widgets"
3. 找到"Hello World Widget"
4. 将其拖拽到桌面

## 编码规范

项目遵循 [Android Widget 编码规范](.codebuddy/rules/android-widget-coding-rules.mdc)

主要规范包括：
- 命名规范：类、方法、变量命名约定
- 代码结构：模块化和分层架构
- 布局规范：Widget尺寸和布局设计
- 资源管理：图标、字符串、颜色等资源组织
- 性能优化：更新策略和内存管理
- 权限规范：合理的权限申请和使用
- 注释规范：代码注释和文档要求

## 功能特性

### 当前功能（阶段一）
- ✅ 显示"Hello World"文本
- ✅ 2x2单元格大小的Widget
- ✅ 每30分钟自动更新
- ✅ 支持深色模式

### 计划功能
- 多种尺寸支持
- Widget配置界面
- 定时更新服务
- 用户交互功能
- 业务数据展示
- 高级特性

## 开发工具

- **IDE**: Android Studio
- **语言**: Kotlin
- **构建工具**: Gradle
- **测试框架**: JUnit, Espresso
- **最小SDK**: 26 (Android 8.0)
- **目标SDK**: 34 (Android 14)

## 贡献指南

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 LICENSE 文件

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件至：your-email@example.com

---

**注意**: 当前项目处于开发阶段，部分功能可能尚未完善。
