package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName


/*
{"turn":"on", "brightness":22}
 */
data class LightSetDto(

    @SerializedName("turn")
    val turn: String,

    @SerializedName("brightness")
    var brightness: Int
)