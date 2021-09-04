package eu.automateeverything.crypto.coingeckoapi

import java.time.Duration
import java.util.*

enum class ChartRange(val duration: Duration, val calendarField: Int) {
    Week(Duration.ofDays(7), Calendar.WEEK_OF_YEAR),
    Day(Duration.ofDays(1), Calendar.DAY_OF_YEAR),
    Hour(Duration.ofHours(1), Calendar.HOUR_OF_DAY)
}