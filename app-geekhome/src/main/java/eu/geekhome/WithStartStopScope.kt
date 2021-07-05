package eu.geekhome

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract  class WithStartStopScope {
    var startStopScope = restartScope()

    fun start() {
        startStopScope.cancel("Restarting...")
    }

    fun stop() {
        startStopScope.cancel("Stopping ${this.javaClass.name}")
        startStopScope = restartScope()
    }

    private fun restartScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }
}