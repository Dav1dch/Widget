package com.example.androidwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.androidwidget.R

/**
 * Hello World Widget Provider
 * 负责处理Hello World Widget的更新和生命周期事件
 */
class HelloWorldWidgetProvider : AppWidgetProvider() {

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
        val views = RemoteViews(context.packageName, R.layout.hello_world_widget)
        
        // 更新Widget文本
        views.setTextViewText(R.id.text_hello_world, context.getString(R.string.hello_world_text))
        
        // 更新Widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
