package eu.geekhome.services.hardware

import com.google.gson.annotations.SerializedName
import eu.geekhome.services.localization.Resource

data class PortDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("factoryId")
    val factoryId: String,

    @SerializedName("adapterId")
    val adapterId: String,

    @SerializedName("shadowed")
    val shadowed: Boolean,

    @SerializedName("value")
    val value: Resource?,

    @SerializedName("valueType")
    val valueType: String,

    @SerializedName("canRead")
    val canRead: Boolean,

    @SerializedName("canWrite")
    val canWrite: Boolean
)