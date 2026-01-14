package com.example.androidwidget.service

import android.content.ComponentName
import android.content.Context
import android.appwidget.AppWidgetManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.androidwidget.widget.DayProgressWidgetProvider
import java.util.concurrent.TimeUnit

/**
 * Widget更新管理器
 * 负责管理和调度Widget的更新任务
 */
object WidgetUpdateManager {

    private const val DAY_PROGRESS_WORK_NAME = "day_progress_update_work"

    /**
     * 启动当日进度Widget的定期更新
     * @param context 上下文
     */
    fun scheduleDayProgressUpdate(context: Context) {
        // 创建定期工作请求（每次更新时间随机1000-2000秒之间）
        // WorkManager要求最小间隔15分钟
        val randomInterval = (1000..2000).random()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false) // 不限制电池状态
            .setRequiresCharging(false) // 不需要充电
            .setRequiresDeviceIdle(false) // 不需要设备空闲
            .build()

        val workRequest = PeriodicWorkRequestBuilder<DayProgressUpdateService>(
            randomInterval.toLong(), TimeUnit.SECONDS
        ).setConstraints(constraints).build()

        // 调度工作（如果存在则替换）
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAY_PROGRESS_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    /**
     * 取消当日进度Widget的更新
     * @param context 上下文
     */
    fun cancelDayProgressUpdate(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(DAY_PROGRESS_WORK_NAME)
    }

    /**
     * 立即触发一次更新
     * @param context 上下文
     */
    fun triggerImmediateUpdate(context: Context) {
        // 先取消现有的定期任务
        cancelDayProgressUpdate(context)

        // 重新调度
        scheduleDayProgressUpdate(context)

        // 发送广播立即更新
        val updateIntent = android.content.Intent(context, DayProgressWidgetProvider::class.java)
        updateIntent.action = DayProgressWidgetProvider.ACTION_REFRESH
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAppWidgetIds(context))
        context.sendBroadcast(updateIntent)
    }

    /**
     * 获取所有Widget ID
     * @param context 上下文
     * @return Widget ID数组
     */
    private fun getAppWidgetIds(context: Context): IntArray {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = android.content.ComponentName(context, DayProgressWidgetProvider::class.java)
        return appWidgetManager.getAppWidgetIds(componentName)
    }
}
