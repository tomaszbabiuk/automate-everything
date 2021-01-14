package eu.geekhome.services.hardware

data class HardwareAdapterDto (
    val state: AdapterState,
    val lastDiscovery: Long,
    val lastError: String?,
    val ports: Collection<PortDto>
)