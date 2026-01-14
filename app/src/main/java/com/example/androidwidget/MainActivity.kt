package com.example.androidwidget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidwidget.R
import com.example.androidwidget.service.WidgetUpdateManager

/**
 * 主Activity
 * 用于应用启动入口
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化Widget更新任务
        initWidgetUpdates()
    }

    /**
     * 初始化Widget更新
     * 确保Widget能够正常更新
     */
    private fun initWidgetUpdates() {
        try {
            // 启动当日进度Widget的定期更新
            WidgetUpdateManager.scheduleDayProgressUpdate(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        // 每次回到应用时，触发一次更新
        WidgetUpdateManager.triggerImmediateUpdate(this)
    }
}
