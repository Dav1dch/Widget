package com.example.androidwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import com.example.androidwidget.MainActivity
import com.example.androidwidget.R
import com.example.androidwidget.model.DayProgress
import com.example.androidwidget.service.WidgetUpdateManager
import com.example.androidwidget.utils.TimeUtils

/**
 * 当日进度 Widget Provider
 * 负责处理当日进度Widget的更新和生命周期事件
 */
class DayProgressWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_REFRESH = "com.example.androidwidget.action.REFRESH_DAY_PROGRESS"
    }

    /**
     * 更新Widget视图
     * @param context 上下文
     * @param appWidgetManager Widget管理器
     * @param appWidgetIds Widget ID数组
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * 处理广播事件
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (ACTION_REFRESH == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) ?: return
            onUpdate(context, appWidgetManager, appWidgetIds)
        } else if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            // 处理点击更新
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = getAppWidgetIds(context)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    /**
     * 获取所有Widget ID
     * @param context 上下文
     * @return Widget ID数组
     */
    private fun getAppWidgetIds(context: Context): IntArray {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, DayProgressWidgetProvider::class.java)
        return appWidgetManager.getAppWidgetIds(componentName)
    }

    /**
     * 当Widget被启用时调用
     * @param context 上下文
     */
    override fun onEnabled(context: Context) {
        // 启动WorkManager定期更新
        WidgetUpdateManager.scheduleDayProgressUpdate(context)
    }

    /**
     * 当Widget被禁用时调用
     * @param context 上下文
     */
    override fun onDisabled(context: Context) {
        // 取消WorkManager更新任务
        WidgetUpdateManager.cancelDayProgressUpdate(context)
    }

    /**
     * 更新单个Widget
     * @param context 上下文
     * @param appWidgetManager Widget管理器
     * @param appWidgetId Widget ID
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // 计算当日进度
        val dayProgress = TimeUtils.calculateDayProgress()

        // 创建RemoteViews
        val views = RemoteViews(context.packageName, R.layout.day_progress_widget)

        // 显示日期和星期
        val dateText = "${dayProgress.dayOfWeek} ${TimeUtils.formatDate(dayProgress.date)}"
        views.setTextViewText(R.id.text_date_weekday, dateText)
        views.setViewVisibility(R.id.text_date_weekday, View.VISIBLE)

        // 隐藏当前时间
        views.setViewVisibility(R.id.text_current_time, View.GONE)

        // 隐藏已用时间
        views.setViewVisibility(R.id.text_passed_time, View.GONE)

        // 更新进度百分比（精确到小数点后两位）
        val percentageText = String.format("%.2f%%", dayProgress.progressPercentage)
        views.setTextViewText(R.id.text_progress_percentage, percentageText)

        // 更新进度条宽度
        views.setInt(R.id.view_progress_bar, "setMax", 100)
        views.setInt(R.id.view_progress_bar, "setProgress", dayProgress.progressPercentage.toInt())

        // 设置点击事件（更新Widget）
        val intent = Intent(context, DayProgressWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        views.setOnClickPendingIntent(R.id.view_progress_bar, pendingIntent)
        views.setOnClickPendingIntent(R.id.text_progress_percentage, pendingIntent)
        views.setOnClickPendingIntent(R.id.text_date_weekday, pendingIntent)

        // 更新Widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
