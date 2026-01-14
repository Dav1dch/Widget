package com.example.androidwidget.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 当日进度数据模型
 * 用于存储和传递当日时间进度的相关信息
 */
data class DayProgress(
    val date: LocalDate,           // 当前日期
    val dayOfWeek: String,         // 星期几（如："周三"）
    val currentTime: LocalTime,     // 当前时间
    val passedMinutes: Int,         // 已过去分钟数
    val totalMinutes: Int,         // 总分钟数
    val remainingMinutes: Int,      // 剩余分钟数
    val progressPercentage: Float,  // 进度百分比（0-100）
    val workHoursStart: LocalTime,  // 工作开始时间
    val workHoursEnd: LocalTime,    // 工作结束时间
    val isWorkDay: Boolean,        // 是否工作日
    val inWorkHours: Boolean       // 是否在工作时间内
) {
    /**
     * 格式化已用时间为小时和分钟
     */
    fun formatPassedTime(): String {
        val hours = passedMinutes / 60
        val minutes = passedMinutes % 60
        return if (hours > 0) {
            "${hours}小时${minutes}分钟"
        } else {
            "${minutes}分钟"
        }
    }

    /**
     * 格式化剩余时间为小时和分钟
     */
    fun formatRemainingTime(): String {
        val hours = remainingMinutes / 60
        val minutes = remainingMinutes % 60
        return if (hours > 0) {
            "${hours}小时${minutes}分钟"
        } else {
            "${minutes}分钟"
        }
    }

    /**
     * 格式化当前时间为 HH:mm
     */
    fun formatCurrentTime(): String {
        return currentTime.toString().take(5) // 格式为 "14:30"
    }
}
