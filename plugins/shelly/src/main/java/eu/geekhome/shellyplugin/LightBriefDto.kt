package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName

data class LightBriefDto(

    @SerializedName("ison")
    val on: Boolean
)
