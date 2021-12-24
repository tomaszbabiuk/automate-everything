package eu.automateeverything.centralheatingplugin

import java.util.Calendar

class MinimumWorkingTimeCounter(private var lastState: Boolean, private val maxMillis: Long) {
    private var switchedOnMillis: Long = 0
    private var lastOnMillis: Long = 0

    val isExceeded: Boolean
        get() = switchedOnMillis > maxMillis

    fun signal(newState: Boolean) {
        if (newState && lastState) {
            val now = Calendar.getInstance().timeInMillis
            val delta = now - lastOnMillis
            switchedOnMillis += delta
            lastOnMillis = now
        } else {
            reset()
        }
        lastState = newState
    }

    private fun reset() {
        switchedOnMillis = 0
        lastOnMillis = Calendar.getInstance().timeInMillis
    }

    init {
        reset()
    }
}