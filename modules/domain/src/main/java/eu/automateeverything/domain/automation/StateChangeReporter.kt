package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto

interface StateChangeReporter {
    fun reportDeviceStateUpdated(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
    fun reportDeviceValueChange(deviceUnit: ControllerAutomationUnit<*>, instanceDto: InstanceDto)
    fun addListener(listener: StateChangedListener)
    fun removeAllListeners()
}

interface StateChangedListener {
    fun onStateChanged(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
    fun onValueChanged(deviceUnit: ControllerAutomationUnit<*>, instanceDto: InstanceDto)
}