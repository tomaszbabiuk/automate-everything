package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto

interface StateChangeReporter {
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto)
    fun reportDeviceStateUpdated(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto)
    fun addListener(listener: StateChangedListener)
    fun removeAllListeners()
}

interface StateChangedListener {
    fun onChanged(deviceUnit: StateDeviceAutomationUnitBase, instanceDto: InstanceDto)
}