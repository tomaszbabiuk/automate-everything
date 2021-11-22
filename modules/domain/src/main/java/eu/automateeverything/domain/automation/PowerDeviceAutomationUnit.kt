package eu.automateeverything.domain.automation

import eu.automateeverything.domain.hardware.PowerLevel

abstract class PowerDeviceAutomationUnit(nameOfOrigin: String) : AutomationUnit<PowerLevel>(nameOfOrigin) {
    @Throws(Exception::class)
    abstract fun changePowerLevel(level: Int, actor: String? = null)
}