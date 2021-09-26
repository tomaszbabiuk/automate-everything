package eu.automateeverything.domain.configurable

class DurationFieldBuilder : FieldBuilder<Duration> {
    override fun fromPersistableString(value: String?): Duration {
        if (value == null || value.isEmpty()) {
            return Duration(0)
        }

        val segments = value.split(":")
        val hours = segments[0].toInt()
        val minutes = segments[1].toInt()
        val seconds = segments[2].toInt()
        val totalSeconds = hours * 3600 + minutes * 60 + seconds
        return Duration(totalSeconds)
    }

    override fun toPersistableString(value: Duration): String {
        val seconds = value.seconds % 60
        val totalMinutes = value.seconds / 60
        val minutes = totalMinutes % 60
        val hours: Int = totalMinutes / 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}