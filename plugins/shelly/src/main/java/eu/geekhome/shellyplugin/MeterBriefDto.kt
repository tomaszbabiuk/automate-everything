package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName


/*
   "meters":[
      {
         "power":0.00,
         "is_valid":true,
         "timestamp":1575191987,
         "counters":[
            0.000,
            0.000,
            0.000
         ],
         "total":0
      }
   ]
 */
data class MeterBriefDto(

    @SerializedName("power")
    val power: Double
)