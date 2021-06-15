package eu.geekhome.domain.hardware

interface HardwareAdapterFactory {
    fun createAdapters(): List<HardwareAdapter>
    val owningPluginId: String
}