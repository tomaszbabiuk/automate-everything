package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto

interface StateChangeReporter {
    fun reportDeviceUpdated(deviceUnit: AutomationUnit<*>, instance: InstanceDto)
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto)
    fun reportDeviceValueChange(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto)
    fun addListener(listener: StateChangedListener)
    fun removeAllListeners()
}

interface StateChangedListener {
    fun onStateChanged(deviceUnit: StateDeviceAutomationUnit, instance: InstanceDto)
    fun onValueChanged(deviceUnit: ControllerAutomationUnit<*>, instance: InstanceDto)
}