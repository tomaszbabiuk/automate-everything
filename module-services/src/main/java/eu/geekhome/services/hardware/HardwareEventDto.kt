package eu.geekhome.services.hardware

import com.google.gson.annotations.SerializedName

data class HardwareEventDto(

    @SerializedName("factoryId")
    val factoryId: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("no")
    val no: Int
)