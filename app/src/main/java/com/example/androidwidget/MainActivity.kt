package com.example.androidwidget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 主Activity
 * 用于应用启动入口
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
