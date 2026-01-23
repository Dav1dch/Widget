package com.example.androidwidget.model

import java.time.LocalDate

/**
 * 年度进度数据模型
 * 用于存储和传递年度进度相关的所有数据
 */
data class YearProgress(
    val year: Int,
    val totalDays: Int,
    val passedDays: Int,
    val remainingDays: Int,
    val progressPercentage: Float,
    val targetDate: LocalDate? = null,
    val daysToTarget: Int? = null,
    val isLeapYear: Boolean
) {
    /**
     * 格式化已过天数显示
     * @return 格式化后的字符串，如 "123天"
     */
    fun formatPassedDays(): String = "${passedDays}天"

    /**
     * 格式化剩余天数显示
     * @return 格式化后的字符串，如 "243天"
     */
    fun formatRemainingDays(): String = "${remainingDays}天"

    /**
     * 格式化进度百分比显示
     * @return 格式化后的字符串，如 "34%"
     */
    fun formatProgressPercentage(): String = "${String.format("%.1f", progressPercentage)}%"

    /**
     * 格式化年份显示
     * @return 格式化后的字符串，如 "2024年"
     */
    fun formatYear(): String = "${year}年"

    /**
     * 格式化目标日期倒计时显示
     * @return 格式化后的字符串，如 "距离新年：242天"
     */
    fun formatTargetCountdown(): String {
        return if (targetDate != null && daysToTarget != null) {
            val targetName = when (targetDate.monthValue) {
                1 -> if (targetDate.dayOfMonth == 1) "新年" else targetDate.month.name
                12 -> if (targetDate.dayOfMonth == 31) "除夕" else targetDate.month.name
                else -> targetDate.month.name
            }
            "距离$targetName：${daysToTarget}天"
        } else {
            ""
        }
    }
}
