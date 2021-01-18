package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName

/*
   "relays":[
      {
         "ison":true,
         "has_timer":false,
         "overpower":false,
         "default_state":"on",
         "auto_on":0.00,
         "auto_off":0.00,
         "btn_on_url":null,
         "out_on_url":null,
         "out_off_url":null,
         "schedule":false,
         "schedule_rules":[

         ],
         "max_power":2500
      }
   ],
 */
data class RelayResponseDto(

    @SerializedName("ison")
    val on: Boolean
)