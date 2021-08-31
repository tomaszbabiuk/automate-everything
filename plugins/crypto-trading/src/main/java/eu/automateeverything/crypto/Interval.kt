package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource

enum class Interval(val label: Resource) {
    Day(R.interval_day), Hour(R.interval_hour), Week(R.interval_week);

    companion object {
        fun fromString(raw: String): Interval {
            return values().first { it.name == raw }
        }
    }
}