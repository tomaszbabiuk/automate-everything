package eu.geekhome.domain.automation

import eu.geekhome.data.instances.InstanceDto

interface StateChangeReporter {
    fun reportDeviceStateChange(deviceUnit: StateDeviceAutomationUnit, instanceDto: InstanceDto)
}