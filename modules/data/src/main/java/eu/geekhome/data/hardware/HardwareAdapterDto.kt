package eu.geekhome.data.hardware

data class HardwareAdapterDto (
    val id: String,
    val factoryId: String,
    val state: AdapterState,
    val lastDiscovery: Long,
    val lastError: String?,
    val portsCount: Int
)