package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName


/*
{"ison":false,"mode":"white","brightness":22}
 */
data class LightResponseDto(

    @SerializedName("ison")
    val on: Boolean,

    @SerializedName("mode")
    val mode: String,

    @SerializedName("brightness")
    val brightness: Int
)