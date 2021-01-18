package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName

/*
   "cloud":{
      "enabled":false,
      "connected":false
   },
 */
data class CloudBriefDto(
    @SerializedName("enabled")
    val enabled: Boolean
)
