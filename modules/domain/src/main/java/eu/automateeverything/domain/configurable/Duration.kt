package eu.automateeverything.domain.configurable

class Duration(val seconds: Int) {
    val milliseconds = seconds * 1000L
}