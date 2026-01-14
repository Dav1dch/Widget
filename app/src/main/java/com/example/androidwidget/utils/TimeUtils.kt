package com.example.androidwidget.utils

import com.example.androidwidget.model.DayProgress
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * 时间计算工具类
 * 用于计算当日时间进度、判断工作日等
 */
object TimeUtils {

    private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd")
    private val dayOfWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.CHINESE)

    /**
     * 计算当日时间进度
     * @param workStart 工作开始时间，默认 8:00
     * @param workEnd 工作结束时间，默认 18:00
     * @return DayProgress 当日进度数据
     */
    fun calculateDayProgress(
        workStart: LocalTime = LocalTime.of(8, 0),
        workEnd: LocalTime = LocalTime.of(18, 0)
    ): DayProgress {
        val now = java.time.LocalDateTime.now()
        val date = now.toLocalDate()
        val currentTime = now.toLocalTime()

        // 计算总秒数（始终按24小时计算）
        val totalSeconds = 24 * 60 * 60

        // 计算已用秒数
        val passedSeconds = calculatePassedSeconds(date, currentTime, workStart, workEnd)

        // 计算剩余秒数
        val remainingSeconds = (totalSeconds - passedSeconds).coerceAtLeast(0)

        // 计算进度百分比（使用秒数更精确）
        val progress = if (totalSeconds > 0) {
            (passedSeconds.toFloat() / totalSeconds) * 100
        } else {
            0f
        }.coerceIn(0f, 100f)

        return DayProgress(
            date = date,
            dayOfWeek = formatDayOfWeek(date),
            currentTime = currentTime,
            passedMinutes = (passedSeconds / 60).toInt(),
            totalMinutes = (totalSeconds / 60).toInt(),
            remainingMinutes = (remainingSeconds / 60).toInt(),
            progressPercentage = progress,
            workHoursStart = workStart,
            workHoursEnd = workEnd,
            isWorkDay = isWorkDay(date),
            inWorkHours = isInWorkHours(date, currentTime, workStart, workEnd)
        )
    }

    /**
     * 计算已用秒数
     */
    private fun calculatePassedSeconds(
        date: LocalDate,
        currentTime: LocalTime,
        workStart: LocalTime,
        workEnd: LocalTime
    ): Long {
        // 始终从0点开始计算已用秒数
        return Duration.between(LocalTime.MIDNIGHT, currentTime).toSeconds()
    }

    /**
     * 判断是否为工作日
     * @return true 如果是周一到周五
     */
    fun isWorkDay(date: LocalDate): Boolean {
        val dayOfWeek = date.dayOfWeek
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
    }

    /**
     * 判断是否在工作时间内
     */
    fun isInWorkHours(
        date: LocalDate,
        currentTime: LocalTime,
        workStart: LocalTime,
        workEnd: LocalTime
    ): Boolean {
        return isWorkDay(date) && currentTime >= workStart && currentTime < workEnd
    }

    /**
     * 格式化星期
     * @return 如："周三"
     */
    fun formatDayOfWeek(date: LocalDate): String {
        return date.format(dayOfWeekFormatter)
    }

    /**
     * 格式化日期
     * @return 如："01/15"
     */
    fun formatDate(date: LocalDate): String {
        return date.format(dateFormatter)
    }
}
