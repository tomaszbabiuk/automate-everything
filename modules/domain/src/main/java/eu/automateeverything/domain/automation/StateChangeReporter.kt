package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto

interface StateChangeReporter {
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
    fun reportDeviceStateUpdated(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
    fun addListener(listener: StateChangedListener)
    fun removeAllListeners()
}

interface StateChangedListener {
    fun onChanged(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
}