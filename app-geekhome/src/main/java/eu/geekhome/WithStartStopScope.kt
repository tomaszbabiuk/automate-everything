package eu.geekhome

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract  class WithStartStopScope {
    var startStopScope = restartScope()

    open fun start() {
        startStopScope.cancel("Restarting...")
        startStopScope = restartScope()
    }

    open fun stop() {
        startStopScope.cancel("Stopping ${this.javaClass.name}")
    }

    private fun restartScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }
}