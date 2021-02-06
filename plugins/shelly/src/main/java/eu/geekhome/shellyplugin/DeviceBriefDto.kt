package eu.geekhome.shellyplugin

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
    val type: String,
    val hostname: String,
    val num_outputs: Int = 0,
    val num_meters: Int = 0
)