# 功能详细设计文档

## 目录
- [1. 当日进度功能](#1-当日进度功能)
- [2. 年度进度功能](#2-年度进度功能)
- [3. Todo List功能](#3-todo-list功能)
- [4. 记录功能](#4-记录功能)
- [5. Obsidian连接功能](#5-obsidian连接功能)

---

## 1. 当日进度功能

### 1.1 功能描述
实时显示当日时间进度，提升时间管理意识。

### 1.2 功能特性
- 显示当前日期和星期
- 实时时间进度百分比
- 已用时间 / 剩余时间
- 工作时间段设置（如8:00-18:00）
- 工作日/休息日区分
- 分钟级自动更新

### 1.3 数据模型

```kotlin
data class DayProgress(
    val date: LocalDate,
    val dayOfWeek: String,
    val currentTime: LocalTime,
    val passedMinutes: Int,
    val totalMinutes: Int,
    val remainingMinutes: Int,
    val progressPercentage: Float,
    val workHoursStart: LocalTime,
    val workHoursEnd: LocalTime,
    val isWorkDay: Boolean,
    val inWorkHours: Boolean
)
```

### 1.4 Widget布局设计

**最小尺寸（2x2）**
```
┌─────────────────┐
│   周三 01/15    │
│   14:30         │
│  ▓▓▓▓▓▓▓▓░░░ 60%│
│  已用 9.5 小时  │
└─────────────────┘
```

### 1.5 关键计算逻辑

```kotlin
class TimeUtils {
    fun calculateDayProgress(
        workStart: LocalTime = LocalTime.of(8, 0),
        workEnd: LocalTime = LocalTime.of(18, 0)
    ): DayProgress {
        val now = LocalDateTime.now()
        val date = now.toLocalDate()
        val currentTime = now.toLocalTime()

        // 计算总分钟数（24小时或工作时间）
        val totalMinutes = if (isWorkDay(date)) {
            Duration.between(workStart, workEnd).toMinutes().toInt()
        } else {
            24 * 60  // 休息日按24小时计算
        }

        // 计算已用分钟数
        val passedMinutes = if (isWorkDay(date) && currentTime >= workStart) {
            if (currentTime >= workEnd) {
                totalMinutes
            } else {
                Duration.between(workStart, currentTime).toMinutes().toInt()
            }
        } else if (currentTime < workStart) {
            0
        } else {
            Duration.between(LocalTime.MIDNIGHT, currentTime).toMinutes().toInt()
        }

        val remainingMinutes = totalMinutes - passedMinutes
        val progress = (passedMinutes.toFloat() / totalMinutes) * 100

        return DayProgress(
            date = date,
            dayOfWeek = formatDayOfWeek(date),
            currentTime = currentTime,
            passedMinutes = passedMinutes,
            totalMinutes = totalMinutes,
            remainingMinutes = remainingMinutes.coerceAtLeast(0),
            progressPercentage = progress.coerceIn(0f, 100f),
            workHoursStart = workStart,
            workHoursEnd = workEnd,
            isWorkDay = isWorkDay(date),
            inWorkHours = isWorkDay(date) && currentTime >= workStart && currentTime < workEnd
        )
    }

    fun isWorkDay(date: LocalDate): Boolean {
        val dayOfWeek = date.dayOfWeek
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
    }
}
```

### 1.6 更新策略
- 使用 WorkManager 实现每分钟更新
- 低电量模式下降低更新频率
- 息屏时暂停更新

---

## 2. 年度进度功能

### 2.1 功能描述
显示年度时间进度，帮助用户直观地了解当前年份的进度情况。

### 2.2 功能特性
- 显示当前年份（如：2024年）
- 显示已过去天数 / 总天数（如：123/366）
- 显示年度进度百分比（如：已过 34%）
- 进度条可视化展示
- 支持自定义目标日期（如新年、生日）
- 剩余天数倒计时
- 每日自动更新

### 2.3 数据模型

```kotlin
data class YearProgress(
    val year: Int,
    val totalDays: Int,
    val passedDays: Int,
    val remainingDays: Int,
    val progressPercentage: Float,
    val targetDate: LocalDate?,  // 自定义目标日期
    val daysToTarget: Int?,       // 距离目标天数
    val isLeapYear: Boolean
)
```

### 2.4 Widget布局设计

**最小尺寸（2x2）**
```
┌─────────────────┐
│   2024年        │
│  123/366 天     │
│  ▓▓▓▓▓▓░░░ 34% │
│  还剩 243 天    │
└─────────────────┘
```

**中等尺寸（4x2）**
```
┌───────────────────────────────┐
│    2024年年度进度              │
│  已过：123天  剩余：243天      │
│  ▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░  34% │
│  距离新年：242天               │
└───────────────────────────────┘
```

### 2.5 关键计算逻辑

```kotlin
class DateUtils {
    fun calculateYearProgress(year: Int): YearProgress {
        val totalDays = if (isLeapYear(year)) 366 else 365
        val currentDayOfYear = getCurrentDayOfYear()
        val passedDays = currentDayOfYear
        val remainingDays = totalDays - passedDays
        val progress = (passedDays.toFloat() / totalDays) * 100

        return YearProgress(
            year = year,
            totalDays = totalDays,
            passedDays = passedDays,
            remainingDays = remainingDays,
            progressPercentage = progress,
            targetDate = null,
            daysToTarget = null,
            isLeapYear = isLeapYear(year)
        )
    }

    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}
```

### 2.6 存储方案
- 使用 SharedPreferences 保存用户配置（目标日期等）
- 无需持久化计算数据（实时计算）

---

## 3. Todo List功能

### 3.1 功能描述
快速管理待办事项，提高工作效率。

### 3.2 功能特性
- 显示待办事项列表
- 快速添加新待办
- 一键标记完成
- 长按删除待办
- 优先级（高/中/低）
- 截止日期
- 分类标签
- 完成进度统计

### 3.3 数据模型

```kotlin
@Entity(tableName = "todos")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: LocalDate? = null,
    val tags: String = "",  // 逗号分隔的标签
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null,
    val widgetId: Int  // 关联的Widget ID
)

enum class Priority {
    HIGH, MEDIUM, LOW
}
```

### 3.4 数据库设计

```kotlin
@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE widgetId = :widgetId ORDER BY priority DESC, dueDate ASC")
    suspend fun getTodosByWidget(widgetId: Int): List<TodoItem>

    @Query("SELECT * FROM todos WHERE isCompleted = 0 AND widgetId = :widgetId ORDER BY priority DESC, dueDate ASC")
    suspend fun getActiveTodos(widgetId: Int): List<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem): Long

    @Update
    suspend fun update(todo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Long): TodoItem?

    @Query("SELECT COUNT(*) FROM todos WHERE widgetId = :widgetId")
    suspend fun getTodoCount(widgetId: Int): Int

    @Query("SELECT COUNT(*) FROM todos WHERE widgetId = :widgetId AND isCompleted = 1")
    suspend fun getCompletedCount(widgetId: Int): Int
}
```

### 3.5 Widget布局设计

**最小尺寸（2x2）- 紧凑列表**
```
┌─────────────────┐
│   Today         │
│ ✓ 完成会议     │
│ ☐ 写报告       │
│ ☐ 打电话       │
│ 1/3 完成      │
└─────────────────┘
```

**中等尺寸（4x2）- 标准列表**
```
┌───────────────────────────────┐
│   今日待办  1/3 完成         │
│ ━━━━━━━━━━━━ 33%             │
│ ☐ [高] 完成项目文档     今天 │
│ ☐ [中] 准备周会材料     明天│
│ ✓ [低] 回复邮件       昨天   │
│ [+ 添加待办]                 │
└───────────────────────────────┘
```

### 3.6 交互设计
- 点击待办项 → 标记完成/取消完成
- 长按待办项 → 删除确认对话框
- 点击"添加待办" → 打开添加Activity
- 点击Widget空白区域 → 打开待办列表Activity

### 3.7 Repository层

```kotlin
class TodoRepository(private val todoDao: TodoDao) {
    fun getTodosByWidget(widgetId: Int): Flow<List<TodoItem>> {
        return todoDao.getTodosByWidget(widgetId)
    }

    fun getActiveTodos(widgetId: Int): Flow<List<TodoItem>> {
        return todoDao.getActiveTodos(widgetId)
    }

    suspend fun addTodo(todo: TodoItem): Long {
        return todoDao.insert(todo)
    }

    suspend fun updateTodo(todo: TodoItem) {
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: TodoItem) {
        todoDao.delete(todo)
    }

    fun getProgress(widgetId: Int): Flow<Float> {
        return combine(
            todoDao.getTodoCount(widgetId),
            todoDao.getCompletedCount(widgetId)
        ) { total, completed ->
            if (total == 0) 0f else completed.toFloat() / total
        }
    }
}
```

---

## 4. 记录功能

### 4.1 功能描述
快速记录灵感和备忘，不错过任何重要想法。

### 4.2 功能特性
- 快速添加文本记录
- 自动添加时间戳
- 记录分类（工作/生活/学习）
- 标签系统
- 记录搜索和过滤
- 数据导出（JSON/TXT）
- 最近记录快速访问

### 4.3 数据模型

```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val category: Category = Category.OTHER,
    val tags: String = "",  // 逗号分隔的标签
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isPinned: Boolean = false,
    val widgetId: Int  // 关联的Widget ID
)

enum class Category {
    WORK, LIFE, LEARNING, OTHER
}
```

### 4.4 数据库设计

```kotlin
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE widgetId = :widgetId ORDER BY isPinned DESC, createdAt DESC LIMIT 5")
    suspend fun getRecentNotes(widgetId: Int): List<Note>

    @Query("SELECT * FROM notes WHERE widgetId = :widgetId ORDER BY isPinned DESC, createdAt DESC")
    suspend fun getAllNotes(widgetId: Int): List<Note>

    @Query("SELECT * FROM notes WHERE category = :category AND widgetId = :widgetId ORDER BY createdAt DESC")
    suspend fun getNotesByCategory(category: Category, widgetId: Int): List<Note>

    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :tag || '%' AND widgetId = :widgetId ORDER BY createdAt DESC")
    suspend fun getNotesByTag(tag: String, widgetId: Int): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}
```

### 4.5 Widget布局设计

**最小尺寸（2x2）- 输入模式**
```
┌─────────────────┐
│   记录...        │
│ ┌─────────────┐ │
│ │             │ │
│ └─────────────┘ │
│ [工作] [生活]  │
│      [保存]     │
└─────────────────┘
```

**中等尺寸（4x2）- 列表模式**
```
┌───────────────────────────────┐
│   最近记录         [+]       │
│ ─────────────────────────────│
│ 14:30 [工作] 开发新功能     ★ │
│ 10:15 [学习] 阅读技术文档     │
│ 昨天  [生活] 买牛奶           │
│ ─────────────────────────────│
│      [查看全部]               │
└───────────────────────────────┘
```

### 4.6 快速记录流程
1. 用户在Widget输入框输入内容
2. 选择分类和标签
3. 点击保存
4. 数据写入数据库
5. 更新Widget显示最近记录

### 4.7 数据导出

```kotlin
class NoteExporter(private val noteDao: NoteDao) {
    suspend fun exportToJson(widgetId: Int): String {
        val notes = noteDao.getAllNotes(widgetId)
        val gson = Gson()
        return gson.toJson(notes)
    }

    suspend fun exportToTxt(widgetId: Int): String {
        val notes = noteDao.getAllNotes(widgetId)
        return notes.joinToString("\n\n") { note ->
            """
                ${note.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
                [${note.category.name}] ${note.tags}
                ${note.content}
            """.trimIndent()
        }
    }
}
```

---

## 5. Obsidian连接功能

### 5.1 功能描述
连接用户的Obsidian知识库，快速访问和浏览笔记。

### 5.2 功能特性
- 选择Obsidian仓库路径
- 解析Markdown文件
- 提取Frontmatter元数据
- 显示最近笔记
- 笔记内容搜索
- 快速打开笔记（调用Obsidian或其他Markdown编辑器）
- 文件权限管理（SAF）

### 5.3 技术挑战
- Android 11+ 存储访问框架（SAF）
- 文件权限持久化
- Markdown解析
- Frontmatter提取
- 性能优化（增量扫描）

### 5.4 数据模型

```kotlin
data class ObsidianNote(
    val path: String,          // 文件相对路径
    val title: String,         // 从Frontmatter或文件名提取
    val content: String?,      // 笔记内容（可选，按需加载）
    val tags: List<String>,     // 标签
    val modifiedAt: Long,      // 最后修改时间
    val createdAt: Long,       // 创建时间
    val frontmatter: Map<String, Any>?  // Frontmatter数据
)

data class ObsidianConfig(
    val vaultPath: String,      // 仓库路径
    val lastScanTime: Long,     // 最后扫描时间
    val isPermissionGranted: Boolean  // 权限状态
)
```

### 5.5 文件解析器

```kotlin
class ObsidianParser {
    fun parseMarkdownFile(file: File): ObsidianNote {
        val content = file.readText()
        val (frontmatter, body) = extractFrontmatter(content)
        val title = extractTitle(frontmatter, file.nameWithoutExtension)
        val tags = extractTags(content)

        return ObsidianNote(
            path = file.relativePath,
            title = title,
            content = body,
            tags = tags,
            modifiedAt = file.lastModified(),
            createdAt = file.createdAt(),
            frontmatter = frontmatter
        )
    }

    fun extractFrontmatter(content: String): Pair<Map<String, Any>?, String> {
        val pattern = Regex("""^---\s*\n(.*?)\n---\s*\n(.*)""", setOf(RegexOption.DOT_MATCHES_ALL))
        val match = pattern.find(content)

        return if (match != null) {
            val yaml = match.groupValues[1]
            val body = match.groupValues[2]
            val frontmatter = parseYaml(yaml)
            Pair(frontmatter, body)
        } else {
            Pair(null, content)
        }
    }

    private fun extractTags(content: String): List<String> {
        // 匹配 #tag 格式
        val tagPattern = Regex("#([\\w-]+)")
        return tagPattern.findAll(content).map { it.groupValues[1] }.distinct()
    }
}
```

### 5.6 存储访问框架（SAF）集成

```kotlin
class ObsidianManager(private val context: Context) {
    private val requestOpenDocumentTree =
        context.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                saveVaultUri(uri)
                scanVault(uri)
            }
        }

    fun requestVaultPermission() {
        requestOpenDocumentTree.launch(null)
    }

    private fun saveVaultUri(uri: Uri) {
        val prefs = context.getSharedPreferences("obsidian_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("vault_uri", uri.toString()).apply()
    }

    private fun loadVaultUri(): Uri? {
        val prefs = context.getSharedPreferences("obsidian_prefs", Context.MODE_PRIVATE)
        val uriString = prefs.getString("vault_uri", null)
        return uriString?.let { Uri.parse(it) }
    }

    private fun scanVault(uri: Uri) {
        val documentsTree = DocumentFile.fromTreeUri(context, uri)
        documentsTree?.listFiles()?.forEach { file ->
            if (file.type == "text/markdown" || file.name?.endsWith(".md") == true) {
                // 解析Markdown文件
                val content = readMarkdownFile(file.uri)
                val note = ObsidianParser().parseMarkdownContent(content, file.name)
                // 保存到数据库或缓存
            }
        }
    }
}
```

### 5.7 Widget布局设计

**最小尺寸（2x2）**
```
┌─────────────────┐
│   Obsidian      │
│ ───────────────│
│ 📄 项目规划     │
│ 📄 读书笔记     │
│ ───────────────│
│   查看更多...   │
└─────────────────┘
```

**中等尺寸（4x2）**
```
┌───────────────────────────────┐
│   Obsidian 最近笔记       🔍 │
│ ─────────────────────────────│
│ 📄 项目规划 #工作 #重要       │
│    更新于 2小时前             │
│ ─────────────────────────────│
│ 📄 学习笔记 #学习 #kotlin     │
│    更新于 昨天                │
│ ─────────────────────────────│
│ 📄 生活随笔 #生活             │
│    更新于 2天前               │
└───────────────────────────────┘
```

### 5.8 配置界面

```kotlin
class ObsidianConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obsidian_config)

        // 选择仓库按钮
        val selectVaultButton = findViewById<Button>(R.id.btn_select_vault)
        selectVaultButton.setOnClickListener {
            obsidianManager.requestVaultPermission()
        }

        // 扫描按钮
        val scanButton = findViewById<Button>(R.id.btn_scan)
        scanButton.setOnClickListener {
            obsidianManager.scanCurrentVault()
        }
    }
}
```

### 5.9 性能优化策略

1. **增量扫描**：只扫描修改过的文件
2. **懒加载**：笔记内容按需加载
3. **缓存机制**：缓存解析结果
4. **索引优化**：建立搜索索引
5. **后台处理**：使用Coroutines异步处理

```kotlin
class ObsidianIndexManager(private val context: Context) {
    private val noteCache = LRUCache<String, ObsidianNote>(100)

    suspend fun searchNotes(query: String): List<ObsidianNote> {
        // 从索引中搜索
        return buildList {
            // 实现搜索逻辑
        }
    }

    suspend fun getNoteContent(notePath: String): String? {
        // 从缓存获取
        noteCache[notePath]?.let { return it.content }

        // 从文件读取
        val file = getFileByPath(notePath)
        val content = file?.readText()
        return content
    }
}
```

---

## 附录

### A. 统一设计规范

#### 颜色方案
- 主色调：#6200EE（紫色）
- 次要色：#03DAC5（青色）
- 背景：#FFFFFF（白色）/ #121212（深色）
- 文字：#000000（黑色）/ #FFFFFF（白色）

#### 字体大小
- 标题：18sp
- 副标题：16sp
- 正文：14sp
- 辅助文字：12sp

#### 间距规范
- Widget内边距：16dp
- 元素间距：8dp
- 圆角半径：8dp

### B. 数据库迁移策略

使用Room的Migration API处理数据库版本升级：

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todos ADD COLUMN tags TEXT DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content TEXT NOT NULL, category TEXT NOT NULL, tags TEXT NOT NULL, createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL, isPinned INTEGER NOT NULL, widgetId INTEGER NOT NULL)")
    }
}
```

### C. 权限需求汇总

```xml
<!-- 存储权限（Obsidian文件访问） -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    tools:ignore="ScopedStorage" />

<!-- 后台服务权限 -->
<uses-permission android:name="android.permission.WAKE_LOCK" />

<!-- 网络权限（可选，云同步） -->
<uses-permission android:name="android.permission.INTERNET" />

<!-- 闹钟权限（定时更新） -->
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM"
    android:minSdkVersion="33" />

<!-- 通知权限（可选） -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"
    android:minSdkVersion="33" />
```
