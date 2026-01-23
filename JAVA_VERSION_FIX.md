# Java 版本兼容性问题解决方案

## 问题描述

编译时出现错误：
```
java.lang.IllegalArgumentException: 25.0.1
    at org.jetbrains.kotlin.com.intellij.util.lang.JavaVersion.parse(JavaVersion.java:298)
```

**原因**：系统上安装了 Java 25.0.1（通过 Homebrew 安装），而 Kotlin 编译器无法识别这个版本号。

## 解决方案

### 方案一：安装 JDK 17（推荐）

**macOS 使用 Homebrew 安装：**
```bash
brew install openjdk@17

# 设置 JAVA_HOME
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@17"' >> ~/.zshrc

# 重新加载配置
source ~/.zshrc

# 验证安装
java -version
```

**macOS 手动下载安装：**
1. 访问 [Oracle JDK 17 下载页](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. 下载 macOS 版本的 JDK 17
3. 安装并设置为默认 Java

### 方案二：在项目中配置 Java 工具链

如果不想修改系统 Java 版本，可以在项目中配置 Gradle 使用特定的 JDK。

1. 修改 `app/build.gradle.kts`：
```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // 添加 Java 工具链配置
    compileSdk = 34
    
    // 配置 Gradle 使用特定的 JDK
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}
```

2. 修改 `build.gradle.kts`（项目根目录）：
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}

// 配置 Java 工具链
allprojects {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
```

### 方案三：使用 Android Studio 的内置 JDK

如果您使用 Android Studio，可以配置项目使用 Android Studio 内置的 JDK：

1. 打开 Android Studio
2. 进入 `File` > `Project Structure` > `SDK Location`
3. 选择 `Use embedded JDK` 或选择 JDK 17 路径
4. 点击 `Apply` 和 `OK`

### 方案四：使用 Gradle 的 Java 工具链自动下载（需要 JDK 17 或更高）

在 `build.gradle.kts` 中配置：

```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}

// 配置 Java 工具链
kotlin {
    jvmToolchain(17)
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}
```

然后在 `gradle.properties` 中添加：
```properties
org.gradle.java.installations.auto-detect=true
org.gradle.java.installations.auto-download=true
```

## 验证解决方案

### 检查 Java 版本
```bash
java -version
```

应该显示：
```
openjdk version "17.x.x" ...
```

### 检查 JAVA_HOME
```bash
echo $JAVA_HOME
```

应该指向 JDK 17 的安装路径。

### 清理并重新构建
```bash
cd /Users/david/Codes/Widget
./gradlew clean
./gradlew build
```

## 临时解决方案（仅用于测试）

如果只是想快速测试代码，可以暂时忽略编译错误，直接在 Android Studio 中：

1. 打开 Android Studio
2. 打开项目 `/Users/david/Codes/Widget`
3. Android Studio 会自动检测并处理 Java 版本问题
4. 使用 Android Studio 的构建系统而不是命令行

## 推荐方案

**推荐使用方案一（安装 JDK 17）**，因为：
- JDK 17 是 LTS（长期支持）版本
- Kotlin 和 Android Gradle Plugin 8.2.0 完全支持
- 稳定性高，兼容性好
- 符合 Android 开发的最佳实践

## 后续步骤

1. 按照方案一安装 JDK 17
2. 重启终端或 IDE
3. 运行 `./gradlew clean`
4. 运行 `./gradlew build`
5. 如果成功，提交修复后的配置文件

## 相关资源

- [Kotlin 兼容的 Java 版本](https://kotlinlang.org/docs/java-interop.html#java-version)
- [Android Gradle Plugin 兼容性](https://developer.android.com/studio/releases/gradle-plugin)
- [Oracle JDK 17 下载](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [OpenJDK 17 下载](https://openjdk.org/projects/jdk/17/)

---

**注意**：Java 25 是一个非常新的版本（可能是预览版或早期版本），Kotlin 编译器可能还没有完全支持。建议在生产环境中使用稳定的 LTS 版本（如 JDK 17 或 JDK 21）。
