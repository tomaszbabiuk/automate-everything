package eu.geekhome.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract  class WithStartStopScope {
    var startStopScope = createNewScope()

    open fun start() {
        startStopScope = createNewScope()
    }

    open fun stop() {
        startStopScope?.cancel("Stopping ${this.javaClass.name}")
    }

    private fun createNewScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }
}