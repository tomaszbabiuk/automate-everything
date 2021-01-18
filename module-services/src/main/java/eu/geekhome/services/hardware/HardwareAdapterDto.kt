package eu.geekhome.services.hardware

import com.google.gson.annotations.SerializedName

data class HardwareAdapterDto (
    @SerializedName("id")
    val id: String,

    @SerializedName("factoryId")
    val factoryId: String,

    @SerializedName("state")
    val state: AdapterState,

    @SerializedName("lastDiscovery")
    val lastDiscovery: Long,

    @SerializedName("lastError")
    val lastError: String?,

    @SerializedName("ports")
    val ports: Collection<PortDto>
)