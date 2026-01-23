package com.example.androidwidget.utils

import com.example.androidwidget.model.YearProgress
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * 日期工具类
 * 提供年度进度计算相关的静态方法
 */
object DateUtils {

    /**
     * 计算年度进度
     * @param year 年份，默认为当前年份
     * @return YearProgress对象，包含年度进度相关信息
     */
    fun calculateYearProgress(year: Int = LocalDate.now().year): YearProgress {
        val isLeapYear = isLeapYear(year)
        val totalDays = if (isLeapYear) 366 else 365
        
        // 获取当前日期在一年中的天数（1-366）
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        
        // 如果传入的年份与当前年份不同，返回该年份的总天数信息
        if (year != currentYear) {
            val passedDays = 0
            val remainingDays = totalDays
            val progressPercentage = 0f
            
            return YearProgress(
                year = year,
                totalDays = totalDays,
                passedDays = passedDays,
                remainingDays = remainingDays,
                progressPercentage = progressPercentage,
                targetDate = null,
                daysToTarget = null,
                isLeapYear = isLeapYear
            )
        }
        
        // 计算当前已过的天数
        val dayOfYear = currentDate.dayOfYear
        val passedDays = dayOfYear
        val remainingDays = totalDays - passedDays
        
        // 计算进度百分比
        val progressPercentage = (passedDays.toFloat() / totalDays) * 100
        
        // 设置默认目标日期为下一年的1月1日（新年）
        val targetDate = LocalDate.of(year + 1, 1, 1)
        val daysToTarget = ChronoUnit.DAYS.between(currentDate, targetDate).toInt()
        
        return YearProgress(
            year = year,
            totalDays = totalDays,
            passedDays = passedDays,
            remainingDays = remainingDays,
            progressPercentage = progressPercentage.coerceIn(0f, 100f),
            targetDate = targetDate,
            daysToTarget = daysToTarget.coerceAtLeast(0),
            isLeapYear = isLeapYear
        )
    }

    /**
     * 计算年度进度（带自定义目标日期）
     * @param year 年份
     * @param targetDate 目标日期
     * @return YearProgress对象
     */
    fun calculateYearProgressWithTarget(year: Int, targetDate: LocalDate): YearProgress {
        val progress = calculateYearProgress(year)
        
        // 计算距离目标日期的天数
        val currentDate = LocalDate.now()
        val daysToTarget = if (targetDate.isAfter(currentDate)) {
            ChronoUnit.DAYS.between(currentDate, targetDate).toInt()
        } else if (targetDate.isBefore(currentDate)) {
            ChronoUnit.DAYS.between(currentDate, targetDate).toInt()
        } else {
            0
        }
        
        return progress.copy(
            targetDate = targetDate,
            daysToTarget = daysToTarget
        )
    }

    /**
     * 判断是否为闰年
     * @param year 年份
     * @return true表示是闰年，false表示不是
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * 获取当前日期在一年中的天数
     * @return 天数（1-366）
     */
    fun getCurrentDayOfYear(): Int {
        return LocalDate.now().dayOfYear
    }

    /**
     * 格式化年份显示
     * @param year 年份
     * @return 格式化后的字符串，如 "2024年"
     */
    fun formatYear(year: Int): String {
        return "${year}年"
    }
}
