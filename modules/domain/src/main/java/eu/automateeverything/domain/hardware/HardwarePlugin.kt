package eu.automateeverything.domain.hardware

import org.pf4j.PluginWrapper

abstract class HardwarePlugin(wrapper: PluginWrapper) : BasicPlugin(wrapper) {
    abstract fun createAdapters(): List<HardwareAdapter<*>>
}

