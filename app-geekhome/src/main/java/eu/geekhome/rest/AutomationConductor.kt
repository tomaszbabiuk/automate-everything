package eu.geekhome.rest

import eu.geekhome.HardwareManager

class AutomationConductor(val hardwareManager: HardwareManager, var enabled: Boolean = false) {

    fun enable() {
        enabled = true
        println("Enabling automation")
    }

    fun disable() {
        enabled = false
        println("Disabling automation")
    }
}