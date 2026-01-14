# Android Widget

一个集成年度进度、当日进度、Todo List、记录和Obsidian文件连接功能的Android桌面小组件应用。

## 项目概述

本项目是一个功能丰富的生产力工具集合，通过桌面小组件的方式，让用户快速查看和管理重要信息。包含以下核心功能：

- 📊 **年度进度** - 实时显示年度时间进度，让时光可见
- ⏰ **当日进度** - 展示当日时间流逝，提升时间管理意识
- ✅ **Todo List** - 快速管理和完成待办事项
- 📝 **记录** - 快速记录灵感和备忘
- 🔗 **Obsidian连接** - 连接你的Obsidian知识库，快速访问笔记

## 项目结构

```
AndroidWidget/
├── app/                              # 应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                 # Kotlin源代码
│   │   │   │   └── com/example/androidwidget/
│   │   │   │       ├── MainActivity.kt
│   │   │   │       ├── widget/       # Widget相关类
│   │   │   │       │   ├── HelloWorldWidgetProvider.kt
│   │   │   │       │   ├── YearProgressWidgetProvider.kt
│   │   │   │       │   ├── DayProgressWidgetProvider.kt
│   │   │   │       │   ├── TodoListWidgetProvider.kt
│   │   │   │       │   ├── QuickNoteWidgetProvider.kt
│   │   │   │       │   └── ObsidianWidgetProvider.kt
│   │   │   │       ├── model/       # 数据模型
│   │   │   │       │   ├── TodoItem.kt
│   │   │   │       │   ├── Note.kt
│   │   │   │       │   ├── YearProgress.kt
│   │   │   │       │   ├── DayProgress.kt
│   │   │   │       │   └── ObsidianNote.kt
│   │   │   │       ├── database/    # 数据库相关
│   │   │   │       │   ├── AppDatabase.kt
│   │   │   │       │   ├── TodoDao.kt
│   │   │   │       │   └── NoteDao.kt
│   │   │   │       ├── repository/  # 数据仓库层
│   │   │   │       │   ├── TodoRepository.kt
│   │   │   │       │   └── NoteRepository.kt
│   │   │   │       ├── utils/       # 工具类
│   │   │   │       │   ├── DateUtils.kt
│   │   │   │       │   ├── TimeUtils.kt
│   │   │   │       │   └── ObsidianParser.kt
│   │   │   │       └── service/     # 服务类
│   │   │   │           └── WidgetUpdateService.kt
│   │   │   ├── res/                  # 资源文件
│   │   │   │   ├── layout/           # 布局文件
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── hello_world_widget.xml
│   │   │   │   │   ├── year_progress_widget.xml
│   │   │   │   │   ├── day_progress_widget.xml
│   │   │   │   │   ├── todo_list_widget.xml
│   │   │   │   │   ├── quick_note_widget.xml
│   │   │   │   │   └── obsidian_widget.xml
│   │   │   │   ├── values/           # 值资源
│   │   │   │   ├── drawable/         # 图形资源
│   │   │   │   ├── xml/              # XML配置
│   │   │   │   └── mipmap-*/         # 图标资源
│   │   │   └── AndroidManifest.xml   # 应用清单
│   ├── build.gradle.kts             # 应用级构建配置
│   └── proguard-rules.pro           # ProGuard规则
├── build.gradle.kts                  # 项目级构建配置
├── settings.gradle.kts              # Gradle设置
├── gradle.properties                # Gradle属性
├── DEVELOPMENT_PLAN.md              # 开发规划
└── README.md                        # 项目说明
```

## 开发进度

### 当前阶段：阶段二 - 当日进度功能实现

**已完成：**
- ✅ 阶段一：Hello World 基础框架
- ✅ 项目结构搭建
- ✅ 编码规范制定
- ✅ 开发规划文档
- ✅ 功能设计文档

**进行中：**
- 🔄 当日进度数据模型设计
- 🔄 时间计算工具类开发
- 🔄 当日进度Widget布局设计

### 开发路线图

| 阶段 | 功能 | 状态 |
|------|------|------|
| 阶段一 | Hello World 基础框架 | ✅ 已完成 |
| 阶段二 | 当日进度功能 | 🔄 进行中 |
| 阶段三 | 年度进度功能 | ⏳ 待开始 |
| 阶段四 | Todo List 功能 | ⏳ 待开始 |
| 阶段五 | 记录功能 | ⏳ 待开始 |
| 阶段六 | Obsidian连接功能 | ⏳ 待开始 |
| 阶段七 | Widget配置与设置 | ⏳ 待开始 |
| 阶段八 | 交互功能实现 | ⏳ 待开始 |
| 阶段九 | 后台服务与更新机制 | ⏳ 待开始 |
| 阶段十 | UI优化阶段 | ⏳ 待开始 |
| 阶段十一 | 数据迁移与兼容性 | ⏳ 待开始 |
| 阶段十二 | 测试与优化 | ⏳ 待开始 |
| 阶段十三 | 发布准备 | ⏳ 待开始 |

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

### 核心功能

#### ⏰ 当日进度 Widget
- 实时显示当日时间进度
- 支持工作时间段设置
- 已用/剩余时间显示
- 分钟级自动更新
- 工作日/休息日区分

#### 📊 年度进度 Widget
- 显示当前年份和已过去天数
- 年度进度百分比可视化
- 剩余天数统计
- 支持自定义目标日期
- 每日自动更新

#### ✅ Todo List Widget
- 待办事项列表展示
- 快速添加/删除待办
- 一键标记完成
- 优先级和截止日期
- 完成进度统计
- 分类标签支持

#### 📝 记录 Widget
- 快速文本记录
- 自动时间戳
- 记录分类（工作/生活/学习）
- 标签系统
- 记录搜索和过滤
- 数据导出功能

#### 🔗 Obsidian Widget
- 连接Obsidian知识库
- 解析Markdown笔记
- 显示最近笔记
- 笔记内容搜索
- 快速打开笔记
- 文件权限管理

### 当前功能（阶段一）
- ✅ Hello World 基础Widget
- ✅ 2x2单元格大小
- ✅ 自动更新机制
- ✅ 完整的项目框架

## 开发工具

### 核心技术
- **IDE**: Android Studio Hedgehog (2023.1.1)+
- **语言**: Kotlin
- **构建工具**: Gradle 8.2
- **最小SDK**: 26 (Android 8.0)
- **目标SDK**: 34 (Android 14)

### 主要库和框架
- **UI**: Material Design Components
- **数据库**: Room Database
- **后台任务**: WorkManager
- **异步处理**: Kotlin Coroutines & Flow
- **依赖注入**: Koin (可选)
- **Markdown解析**: MarkdownJ / CommonMark
- **JSON处理**: Gson / kotlinx.serialization

### 测试框架
- **单元测试**: JUnit 4
- **UI测试**: Espresso
- **Mock**: Mockk

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
