package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName

/*
   "device":{
      "type":"SHPLG-S",
      "mac":"CC50E3376CC2",
      "hostname":"shellyplug-s-376CC2",
      "num_outputs":1,
      "num_meters":1
   }
 */
data class DeviceBriefDto(
    @SerializedName("type")
    val type: String,

    @SerializedName("hostname")
    val hostname: String,

    @SerializedName("num_outputs")
    val num_outputs: Int = 0,

    @SerializedName("num_meters")
    val num_meters: Int = 0
)