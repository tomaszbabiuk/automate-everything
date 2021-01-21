package eu.geekhome.services.hardware

import com.google.gson.annotations.SerializedName
import eu.geekhome.services.localization.Resource

data class PortDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("shadowed")
    val shadowed: Boolean,

    @SerializedName("value")
    val value: Resource?,

    @SerializedName("canRead")
    val canRead: Boolean,

    @SerializedName("canWrite")
    val canWrite: Boolean
)