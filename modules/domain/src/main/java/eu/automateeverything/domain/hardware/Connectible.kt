package eu.automateeverything.domain.hardware

import java.util.*

interface Connectible {
    var connectionValidUntil : Long

    fun updateValidUntil(until : Long) {
        connectionValidUntil = until
    }

    fun markDisconnected() {
        connectionValidUntil = 0
    }

    fun checkIfConnected(now: Calendar): Boolean {
        return now.timeInMillis < connectionValidUntil
    }
}