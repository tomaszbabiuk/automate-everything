package eu.geekhome.domain.inbox

interface Inbox {

    fun sendCustomMessage(message: String)
    fun sendAppStarted()
    fun sendNewPortDiscovered(newPortId: String)
    fun sendAutomationStarted()
    fun sendAutomationStopped()
}

