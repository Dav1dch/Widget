package com.example.androidwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.example.androidwidget.MainActivity
import com.example.androidwidget.R
import com.example.androidwidget.model.YearProgress
import com.example.androidwidget.utils.DateUtils

/**
 * 年度进度 Widget Provider
 * 负责处理年度进度Widget的更新和生命周期事件
 */
class YearProgressWidgetProvider : AppWidgetProvider() {

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
     * 当Widget被添加到桌面时调用
     * @param context 上下文
     */
    override fun onEnabled(context: Context) {
        // Widget首次启用时的初始化工作
    }

    /**
     * 当最后一个Widget被移除时调用
     * @param context 上下文
     */
    override fun onDisabled(context: Context) {
        // 清理资源，取消定时任务等
    }

    /**
     * 当Widget配置改变时调用（例如大小改变）
     * @param context 上下文
     * @param appWidgetManager Widget管理器
     * @param appWidgetId Widget ID
     * @param newOptions 新的配置选项
     */
    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        // 当Widget尺寸改变时重新更新
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    /**
     * 接收广播事件
     * @param context 上下文
     * @param intent 意图
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // 处理自定义广播事件
        when (intent.action) {
            ACTION_UPDATE_YEAR_PROGRESS -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, YearProgressWidgetProvider::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
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
        val views = RemoteViews(context.packageName, R.layout.year_progress_widget)
        
        // 计算年度进度数据
        val yearProgress = DateUtils.calculateYearProgress()
        
        // 更新Widget各个视图组件
        views.setTextViewText(R.id.text_year, yearProgress.formatYear())
        views.setTextViewText(
            R.id.text_days_info,
            "${yearProgress.passedDays}/${yearProgress.totalDays} 天"
        )
        views.setTextViewText(
            R.id.text_percentage,
            yearProgress.formatProgressPercentage()
        )
        views.setTextViewText(
            R.id.text_remaining,
            yearProgress.formatRemainingDays()
        )
        
        // 更新进度条
        views.setProgressBar(
            R.id.progress_year,
            100,
            yearProgress.progressPercentage.toInt(),
            false
        )
        
        // 设置点击事件：点击Widget打开主Activity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.text_year, pendingIntent)
        
        // 更新Widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        /**
         * 更新年度进度Widget的广播Action
         */
        const val ACTION_UPDATE_YEAR_PROGRESS = 
            "com.example.androidwidget.action.UPDATE_YEAR_PROGRESS"
        
        /**
         * 手动触发所有年度进度Widget更新
         * @param context 上下文
         */
        fun updateAllWidgets(context: Context) {
            val intent = Intent(ACTION_UPDATE_YEAR_PROGRESS).apply {
                setPackage(context.packageName)
            }
            context.sendBroadcast(intent)
        }
    }
}
