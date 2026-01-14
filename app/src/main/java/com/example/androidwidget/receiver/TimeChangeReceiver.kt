package com.example.androidwidget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import com.example.androidwidget.widget.DayProgressWidgetProvider

/**
 * 时间变化广播接收器
 * 监听系统时间变化，立即更新Widget
 */
class TimeChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 当时间改变时，更新所有当日进度Widget
        when (intent.action) {
            Intent.ACTION_TIME_TICK -> {
                // 每分钟触发一次
                updateDayProgressWidgets(context)
            }
            Intent.ACTION_TIME_CHANGED -> {
                // 用户手动修改时间
                updateDayProgressWidgets(context)
            }
            Intent.ACTION_DATE_CHANGED -> {
                // 日期改变
                updateDayProgressWidgets(context)
            }
            Intent.ACTION_TIMEZONE_CHANGED -> {
                // 时区改变
                updateDayProgressWidgets(context)
            }
            Intent.ACTION_SCREEN_ON -> {
                // 屏幕亮起时更新
                updateDayProgressWidgets(context)
            }
        }
    }

    /**
     * 更新所有当日进度Widget
     */
    private fun updateDayProgressWidgets(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = android.content.ComponentName(context, DayProgressWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            if (appWidgetIds.isNotEmpty()) {
                // 发送广播更新Widget
                val updateIntent = Intent(context, DayProgressWidgetProvider::class.java)
                updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                context.sendBroadcast(updateIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
